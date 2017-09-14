package ru.report.rest;

import ru.metal.api.report.OrderReport;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by User on 07.09.2017.
 */
@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ReportService {
    @EJB(lookup = "ejb:report-service-ear/report-service-impl/orderReport!ru.metal.api.report.OrderReport")
    private OrderReport orderReport;

    @POST
    @Path("/")
    public Response getOrderPdf(OrderReportRequest orderReportRequest) throws Exception {
        OrderReportResponse jasperReports = orderReport.createJasperReport(orderReportRequest);
        return Response.ok(jasperReports).build();
    }

}
