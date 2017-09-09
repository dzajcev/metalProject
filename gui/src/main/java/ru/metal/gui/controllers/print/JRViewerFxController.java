package ru.metal.gui.controllers.print;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import ru.metal.gui.controllers.AbstractController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;


/**
 * @author Michael Grecol
 * @project JasperViewerFx
 * @filename JRViewerFxController.java
 * @date Mar 23, 2015
 */
public class JRViewerFxController extends AbstractController {
    //private final Stage dialog;
    @FXML
    private Button print, save, prevPage, firstPage, nextPage, lastPage, zoomIn, zoomOut;
    @FXML
    private Label bottomLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField txtPage;

    private JasperPrint jprint;

    private int imageHeight = 0, imageWidth = 0, reportPages = 0;

    // Property
    private IntegerProperty currentPage;


    @FXML
    protected Node view;


    public void show() {
        setCurrentPage(1);

        // Bottom label
        bottomLabel.setText("Page 1 of " + reportPages);

        // Visual feedback of reading progress
        currentPage.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            bottomLabel.setText("Page " + newValue + " of " + reportPages);
            txtPage.setText(newValue.toString());

            if (newValue.intValue() == 1) {
                prevPage.setDisable(true);
                firstPage.setDisable(true);
            }

            if (newValue.intValue() == reportPages) {
                nextPage.setDisable(true);
                lastPage.setDisable(true);
            }
        });

        // Those buttons must start disabled
        prevPage.setDisable(true);
        firstPage.setDisable(true);

        // With only one page those buttons are unnecessary
        if (reportPages == 1) {
            nextPage.setDisable(true);
            lastPage.setDisable(true);
        }

        txtPage.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    int p = Integer.parseInt(txtPage.getText());
                    if (p > reportPages) {
                        setCurrentPage(reportPages);
                    } else {
                        if (p > 0) {
                            setCurrentPage(p);
                        } else {
                            setCurrentPage(1);
                        }
                    }
                } catch (NumberFormatException e) {
                    Alert dialog1 = new Alert(Alert.AlertType.WARNING, "Invalid number", ButtonType.OK);
                    dialog1.show();
                }
            }
        });
    }

    @FXML
    private void firstPage() {
        setCurrentPage(1);

        // Turn foward buttons on again
        if (nextPage.isDisabled()) {
            nextPage.setDisable(false);
            lastPage.setDisable(false);
        }
    }

    @FXML
    private void prevPage() {
        int newValue = getCurrentPage() - 1;
        setCurrentPage(newValue);

        // Turn foward buttons on again
        if (nextPage.isDisabled()) {
            nextPage.setDisable(false);
            lastPage.setDisable(false);
        }
    }

    @FXML
    private void nextPage() {
        int newValue = getCurrentPage() + 1;
        setCurrentPage(newValue);

        // Turn previous button on again
        if (prevPage.isDisabled()) {
            prevPage.setDisable(false);
            firstPage.setDisable(false);
        }
    }

    @FXML
    private void lastPage() {
        setCurrentPage(reportPages);

        // Turn previous button on again
        if (prevPage.isDisabled()) {
            prevPage.setDisable(false);
            firstPage.setDisable(false);
        }
    }

    @FXML
    private void zoomIn() {
        imageView.setScaleX(imageView.getScaleX() + 0.15);
        imageView.setScaleY(imageView.getScaleY() + 0.15);
        imageView.setFitHeight(imageHeight + 0.15);
        imageView.setFitWidth(imageWidth + 0.15);
    }

    @FXML
    private void zoomOut() {
        imageView.setScaleX(imageView.getScaleX() - 0.15);
        imageView.setScaleY(imageView.getScaleY() - 0.15);
        imageView.setFitHeight(imageHeight - 0.15);
        imageView.setFitWidth(imageWidth - 0.15);
    }

    public void setCurrentPage(int page) {
        currentPage.set(page);
        viewPage(page);
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    @FXML
    public boolean save() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter pdf = new FileChooser.ExtensionFilter("Portable Document Format (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter html = new FileChooser.ExtensionFilter("HyperText Markup Language", "*.html");
        FileChooser.ExtensionFilter xml = new FileChooser.ExtensionFilter("eXtensible Markup Language", "*.xml");
        FileChooser.ExtensionFilter xls = new FileChooser.ExtensionFilter("Microsoft Excel 2007", "*.xls");
        FileChooser.ExtensionFilter xlsx = new FileChooser.ExtensionFilter("Microsoft Excel 2016", "*.xlsx");
        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("Comma-separeted Values", "*.csv");
        chooser.getExtensionFilters().addAll(pdf, html, xml, xls, xlsx, csv);

        chooser.setTitle("Salvar");
        chooser.setSelectedExtensionFilter(pdf);
        File file = chooser.showSaveDialog(getPrimaryStage());

        if (file != null) {
            List<String> box = chooser.getSelectedExtensionFilter().getExtensions();
            switch (box.get(0)) {
                case "*.pdf":
                    try {
                        JasperExportManager.exportReportToPdfFile(jprint, file.getPath());
                    } catch (JRException ex) {
                    }
                    break;
                case "*.html":
                    try {
                        JasperExportManager.exportReportToHtmlFile(jprint, file.getPath());
                    } catch (JRException ex) {
                    }
                    break;
                case "*.xml":
                    try {
                        JasperExportManager.exportReportToXmlFile(jprint, file.getPath(), false);
                    } catch (JRException ex) {
                    }
                    break;
                case "*.xls":
                    try {
                        JRXlsExporter exporter = new JRXlsExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jprint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
                        SimpleXlsxReportConfiguration repConfig = new SimpleXlsxReportConfiguration();
                        Map<String, String> dateFormats = new HashMap<>();
                        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");
                        repConfig.setDetectCellType(Boolean.TRUE);
                        repConfig.setFontSizeFixEnabled(true);
                        repConfig.setFormatPatternsMap(dateFormats);
                        exporter.setConfiguration(repConfig);
                        exporter.exportReport();
                    } catch (JRException ex) {
                    }
                    break;
                case "*.xlsx":
                    try {
                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jprint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
                        SimpleXlsxReportConfiguration repConfig = new SimpleXlsxReportConfiguration();
                        Map<String, String> dateFormats = new HashMap<>();
                        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");
                        repConfig.setDetectCellType(Boolean.TRUE);
                        repConfig.setFontSizeFixEnabled(true);
                        repConfig.setFormatPatternsMap(dateFormats);
                        exporter.setConfiguration(repConfig);
                        exporter.exportReport();
                    } catch (JRException ex) {
                    }
                    break;
                case "*.csv":
                    try {
                        JRCsvExporter exporter = new JRCsvExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jprint));
                        exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
                        exporter.exportReport();
                    } catch (JRException ex) {
                    }
                    break;
            }
        }
        return true;
    }

    private WritableImage getImage(int pageNumber) {
        BufferedImage image = null;
        try {
            image = (BufferedImage) JasperPrintManager.printPageToImage(jprint, pageNumber - 1, 2);
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WritableImage fxImage = new WritableImage(jprint.getPageWidth(), jprint.getPageHeight());
        return SwingFXUtils.toFXImage(image, fxImage);

    }

    private void viewPage(int pageNumber) {
        float zoomFactor = 1.33f;
        imageView.setFitHeight(imageHeight * zoomFactor);
        imageView.setFitWidth(imageWidth * zoomFactor);
        imageView.setImage(getImage(pageNumber));
    }


    public void clear() {
        // TODO Auto-generated method stub

    }

    @FXML
    private void print() {
        try {
            JasperPrintManager.printReport(jprint, true);
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        Image printer = new Image(getClass().getResourceAsStream("/icons/jasperviewer/printer.png"));
        print.setGraphic(new ImageView(printer));

        Image saveImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/save.png"));
        save.setGraphic(new ImageView(saveImage));

        Image firtPageImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/firstimg.png"));
        firstPage.setGraphic(new ImageView(firtPageImage));

        Image prevPageImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/backimg.png"));
        prevPage.setGraphic(new ImageView(prevPageImage));

        Image nextPageImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/nextimg.png"));
        nextPage.setGraphic(new ImageView(nextPageImage));

        Image lastPageImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/lastimg.png"));
        lastPage.setGraphic(new ImageView(lastPageImage));

        Image zoomOutImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/zoomout.png"));
        zoomOut.setGraphic(new ImageView(zoomOutImage));

        Image zoomInImage = new Image(getClass().getResourceAsStream("/icons/jasperviewer/zoomin.png"));
        zoomIn.setGraphic(new ImageView(zoomInImage));
        currentPage = new SimpleIntegerProperty(this, "currentPage");
        currentPage.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            bottomLabel.setText("Page " + newValue + " of " + reportPages);
            txtPage.setText(newValue.toString());

            if (newValue.intValue() == 1) {
                prevPage.setDisable(true);
                firstPage.setDisable(true);
            }

            if (newValue.intValue() == reportPages) {
                nextPage.setDisable(true);
                lastPage.setDisable(true);
            }
        });
        txtPage.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    int p = Integer.parseInt(txtPage.getText());
                    if (p > reportPages) {
                        setCurrentPage(reportPages);
                    } else {
                        if (p > 0) {
                            setCurrentPage(p);
                        } else {
                            setCurrentPage(1);
                        }
                    }
                } catch (NumberFormatException e) {
                    Alert dialog1 = new Alert(Alert.AlertType.WARNING, "Invalid number", ButtonType.OK);
                    dialog1.show();
                }
            }
        });
        // Those buttons must start disabled
        prevPage.setDisable(true);
        firstPage.setDisable(true);
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jprint = jasperPrint;
        reportPages = jprint.getPages().size();
        imageHeight = jprint.getPageHeight() + 284;
        imageWidth = jprint.getPageWidth() + 201;

    }


}
