package ru.metal.report.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 07.09.2017.
 */
public abstract class ReportGenerator {

    protected abstract File getReportTemplate();

    public JasperPrint create(Collection dataSource, Map<String, Object> params) throws JRException {
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataSource);
        params.put("ItemDataSource",itemsJRBean);
         // JasperDesign jasperDesign = JRXmlLoader.load(getReportTemplate());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getReportTemplate());
      //   JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
   JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
       JasperExportManager.exportReportToPdfFile(jasperPrint, "d:\\3.pdf");

//        Map<String, String> dateFormats = new HashMap<>();
//        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");
//        JRXlsxExporter exporter = new JRXlsxExporter();
//        SimpleXlsxReportConfiguration repConfig = new SimpleXlsxReportConfiguration();
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        File fff=new File("d://exxx");
//        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
//                fff.getAbsolutePath() + ".xlsx"));
//        repConfig.setDetectCellType(Boolean.TRUE);
//        repConfig.setFontSizeFixEnabled(true);
//        repConfig.setFormatPatternsMap(dateFormats);
//        exporter.setConfiguration(repConfig);
//        exporter.exportReport();

        return jasperPrint;
    }
}
