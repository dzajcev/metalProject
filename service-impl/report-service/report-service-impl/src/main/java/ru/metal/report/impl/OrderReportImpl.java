package ru.metal.report.impl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import ru.common.api.dto.CellFormats;
import ru.metal.api.auth.AuthorizationFacade;
import ru.metal.api.auth.dto.User;
import ru.metal.api.auth.request.ObtainUserRequest;
import ru.metal.api.auth.response.ObtainUserResponse;
import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.api.contragents.dto.PersonType;
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

    @EJB(lookup = "ejb:auth-service-ear/auth-service-impl/authorizationFacade!ru.metal.api.auth.AuthorizationFacade")
    private AuthorizationFacade authorizationFacade;
    @Override
    public OrderReportResponse createJasperReport(OrderReportRequest reportRequest) {

        ObtainOrderRequest obtainOrderRequest = new ObtainOrderRequest();
        obtainOrderRequest.setGuids(reportRequest.getOrderList());
        ObtainOrderResponse orders = orderFacade.getOrders(obtainOrderRequest);
        OrderReportResponse orderReportResponse = new OrderReportResponse();
        JasperPrint jasperPrint = null;
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
            params.put("orderNumber", dto.getNumber());
            params.put("orderDate", new Date());
            params.put("buyer", dto.getRecipient().getName());
            params.put("recipientAddress", dto.getRecipient().getAddress());
            params.put("recipientInn", dto.getRecipient().getInn());
            params.put("recipientKpp", dto.getRecipient().getKpp());
            params.put("recipientPhone", dto.getRecipient().getPhone());

            params.put("sourceFull", createFullName(dto.getSource()));
            params.put("shipperSourceFull", createFullName(dto.getSource().getShipper() != null ? dto.getSource().getShipper() : dto.getSource()));

            params.put("buyerFull", createFullName(dto.getRecipient()));
            params.put("shipperBuyerFull", createFullName(dto.getRecipient().getShipper() != null ? dto.getRecipient().getShipper() : dto.getRecipient()));

            if (dto.getSource().getDirector() != null) {
                params.put("directorPosition", dto.getSource().getDirector().getPosition() != null ? dto.getSource().getDirector().getPosition() : "");
            } else {
                params.put("directorPosition", "");
            }

            params.put("directorFIO", dto.getSource().getDirector() != null ? dto.getSource().getDirector().getShortName() : "");
            params.put("accountantFIO", dto.getSource().getAccountant() != null ? dto.getSource().getAccountant().getShortName() : "");
            ObtainUserRequest obtainUserRequest=new ObtainUserRequest();
            obtainUserRequest.setGuid(dto.getUserGuid());
            ObtainUserResponse obtainUserResponse = authorizationFacade.obtainUser(obtainUserRequest);
            if (!obtainUserResponse.getDataList().isEmpty()){
                User user = obtainUserResponse.getDataList().get(0);
                params.put("employeeFIO",user.getShortName());
            }
            List<OrderBody> body = new ArrayList<>();
            int i = 1;
            DecimalFormat formatterTwoPlaces = new DecimalFormat(CellFormats.TWO_DECIMAL_PLACES);
            DecimalFormat formatterThreePlaces = new DecimalFormat(CellFormats.THREE_DECIMAL_PLACES);
            double total = 0;
            double nds = 0;

            for (OrderBodyDto bodyDto : dto.getBody()) {
                OrderBody orderBody = new OrderBody();
                orderBody.setNpp(i);
                orderBody.setGoodName(bodyDto.getGood().getName());
                orderBody.setOkei(bodyDto.getGood().getOkei().getName());
                orderBody.setCount(formatterThreePlaces.format(bodyDto.getCount()));
                orderBody.setPrice(formatterTwoPlaces.format(bodyDto.getPrice()));

                double sum = bodyDto.getPrice() * bodyDto.getCount();
                orderBody.setSumma(formatterTwoPlaces.format(sum));
                body.add(orderBody);
                i++;
                nds += sum - sum / new Double(1 + (new Double(bodyDto.getGood().getNds()) / 100));
                total += sum;
            }

            total = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String format = RussianMoney.digits2Text(total);

            params.put("total", formatterTwoPlaces.format(total));
            params.put("nds", formatterTwoPlaces.format(nds));
            params.put("stringTotal", format);
            try {
                if (jasperPrint == null) {
                    jasperPrint = create(body, params);
                } else {
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


    private String createFullName(ContragentDto contragentDto) {
        StringBuilder stringBuilder = new StringBuilder(contragentDto.getName());
        stringBuilder.append(", ИНН ").append(contragentDto.getInn());
        if (contragentDto.getPersonType() == PersonType.UL) {
            stringBuilder.append(", КПП ").append(contragentDto.getKpp());
        }
        stringBuilder.append(", ").append(contragentDto.getAddress());
        if (contragentDto.getPhone() != null) {
            stringBuilder.append(", ").append(contragentDto.getPhone());
        }
        return stringBuilder.toString();

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
