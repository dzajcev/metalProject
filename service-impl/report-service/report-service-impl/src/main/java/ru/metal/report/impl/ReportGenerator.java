package ru.metal.report.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Created by User on 07.09.2017.
 */
public abstract class ReportGenerator {

    protected abstract File getReportTemplate();

    public JasperPrint create(Collection dataSource, Map<String, Object> params) throws JRException {
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataSource);
        params.put("ItemDataSource", itemsJRBean);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getReportTemplate());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfFile(jasperPrint, "d:\\3.pdf");
        return jasperPrint;
    }
}
