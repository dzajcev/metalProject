package ru.metal.api.report;

import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;

/**
 * Created by User on 07.09.2017.
 */
public interface OrderReport {
    OrderReportResponse createJasperReport(OrderReportRequest orderHeaderDto);
}
