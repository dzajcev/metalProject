package ru.metal.report.impl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import ru.metal.api.common.dto.CellFormats;
import ru.metal.api.order.OrderFacade;
import ru.metal.api.order.dto.OrderBodyDto;
import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.api.order.request.ObtainOrderRequest;
import ru.metal.api.order.response.ObtainOrderResponse;
import ru.metal.api.report.OrderReport;
import ru.metal.api.report.dto.order.OrderData;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;
import ru.metal.report.dto.OrderBody;
import ru.metal.report.utils.RussianMoney;

import javax.ejb.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Created by User on 07.09.2017.
 */
@Remote
@Stateless(name = "orderReport")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class OrderReportImpl extends ReportGenerator implements OrderReport {


    @EJB(lookup = "ejb:metal-service-ear/metal-service-impl/orderFacade!ru.metal.api.order.OrderFacade")
    private OrderFacade orderFacade;

    @Override
    public OrderReportResponse createJasperReport(OrderReportRequest reportRequest) {

        ObtainOrderRequest obtainOrderRequest=new ObtainOrderRequest();
        obtainOrderRequest.setGuids(reportRequest.getOrderList());
        ObtainOrderResponse orders = orderFacade.getOrders(obtainOrderRequest);
        OrderReportResponse orderReportResponse = new OrderReportResponse();
        JasperPrint jasperPrint=null;
        for (OrderHeaderDto dto : orders.getDataList()) {
            Map<String, Object> params = new HashMap<>();
            params.put("bankName", dto.getSource().getBank());
            params.put("inn", dto.getSource().getInn());
            params.put("bik", dto.getSource().getBik());
            params.put("kpp", dto.getSource().getKpp());
            params.put("account", dto.getSource().getAccount());
            params.put("korrAccount", dto.getSource().getKorrAccount());
            params.put("address", dto.getSource().getAddress());
            params.put("phone", dto.getSource().getPhone());
            params.put("recipient", dto.getSource().getName());
            params.put("orderNumber", "1111");
            params.put("orderDate", new Date());
            params.put("buyer", dto.getRecipient().getName());
            params.put("recipientAddress", dto.getRecipient().getAddress());
            params.put("recipientInn", dto.getRecipient().getInn());
            params.put("recipientKpp", dto.getRecipient().getKpp());
            params.put("recipientPhone", dto.getRecipient().getPhone());


            List<OrderBody> body = new ArrayList<>();
            int i = 1;
            String pattern = CellFormats.TWO_DECIMAL_PLACES;
            DecimalFormat formatter = new DecimalFormat(pattern);
            double total = 0;
            double nds = 0;

            for (OrderBodyDto bodyDto : dto.getBody()) {
                OrderBody orderBody = new OrderBody();
                orderBody.setNpp(i);
                orderBody.setGoodName(bodyDto.getGood().getName());
                orderBody.setOkei(bodyDto.getGood().getOkei().getName());
                orderBody.setCount(formatter.format(bodyDto.getCount()));
                orderBody.setPrice(formatter.format(bodyDto.getPrice()));

                double sum = bodyDto.getPrice() * bodyDto.getCount();
                orderBody.setSumma(formatter.format(sum));
                body.add(orderBody);
                i++;
                nds += sum - sum / new Double(1 + (new Double(bodyDto.getGood().getNds()) / 100));
                total += sum;
            }

            total = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String format = RussianMoney.digits2Text(total);

            params.put("total", formatter.format(total));
            params.put("nds", formatter.format(nds));
            params.put("stringTotal", format);
            try {
                if (jasperPrint==null) {
                    jasperPrint = create(body, params);
                }else{
                    JasperPrint jp = create(body, params);
                    jasperPrint.getPages().addAll(jp.getPages());
                }

            } catch (JRException e) {
                e.printStackTrace();
            }
        }
        OrderData orderData = new OrderData();
        byte[] serialize = SerializationUtils.serialize(jasperPrint);
        orderData.setJasperData(serialize);
        orderReportResponse.setOrderData(orderData);
        return orderReportResponse;
    }


    @Override
    protected File getReportTemplate() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("/reports/OrderTemplate.jasper");
        try {
            final File tempFile = File.createTempFile("order", "jasper");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(is, out);
            }
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
