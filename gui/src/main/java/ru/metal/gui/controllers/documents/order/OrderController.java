package ru.metal.gui.controllers.documents.order;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.lang3.SerializationUtils;
import ru.common.api.dto.CellFormats;
import ru.metal.api.contragents.dto.ContragentDto;
import ru.metal.api.contragents.dto.ContragentType;
import ru.metal.api.contragents.request.ObtainContragentRequest;
import ru.metal.api.contragents.response.ObtainContragentResponse;
import ru.metal.api.documents.order.dto.OrderHeaderDto;
import ru.metal.api.documents.order.dto.OrderStatus;
import ru.metal.api.documents.order.dto.UpdateBodyResult;
import ru.metal.api.documents.order.dto.UpdateOrderResult;
import ru.metal.api.documents.order.request.UpdateOrderRequest;
import ru.metal.api.documents.order.response.UpdateOrderResponse;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.OrderBodyFx;
import ru.metal.dto.OrderHeaderFx;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.dto.helper.OrderHeaderHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controllers.catalogs.contragents.ContragentInfoController;
import ru.metal.gui.controllers.catalogs.contragents.ContragentsForm;
import ru.metal.gui.controllers.print.JRViewerFxController;
import ru.metal.gui.windows.SaveMenuItem;
import ru.metal.gui.windows.Window;
import ru.metal.rest.ContragentsClient;
import ru.metal.rest.OrderClient;
import ru.metal.rest.ReportClient;
import ru.metal.security.ejb.UserContextHolder;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 30.08.2017.
 */
public class OrderController extends AbstractController {
    private OrderHeaderFx orderHeaderFx;

    @FXML
    private ComboBox<ContragentFx> shipper;

    @FXML
    private ComboBox<ContragentFx> buyer;

    @FXML
    private TextField orderNumber;

    @FXML
    private DatePicker orderDate;

    @FXML
    private TextArea comment;

    @FXML
    private Label status;
    @FXML
    private OrderBodyTable orderBody;

    @FXML
    private Label summa;
    @FXML
    private Label sumWoNds;
    @FXML
    private Label count;
    @FXML
    private Label weight;
    @FXML
    private CheckMenuItem setPaid;

    @FXML
    private SaveMenuItem savePrint;


    @FXML
    private SaveMenuItem save;

    private ContragentsClient contragentsClient;

    private OrderClient orderClient;

    private ReportClient reportClient;


    @FXML
    private void initialize() {
        contragentsClient = new ContragentsClient();
        orderClient = new OrderClient();
        reportClient = new ReportClient();
        savePrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!saveResult(false)) {
                    return;
                }

                OrderHeaderDto entity = orderHeaderFx.getEntity();
                OrderReportRequest orderReportRequest = new OrderReportRequest();
                orderReportRequest.getOrderList().add(entity.getGuid());
                try {
                    OrderReportResponse orders = reportClient.getOrders(orderReportRequest);
                    JasperPrint deserialize = SerializationUtils.deserialize(orders.getOrderData().getJasperData());
                    Window<JRViewerFxController, Node> window = StartPage.openContent("/fxml/FRViewerFx.fxml", getPrimaryStage());
                    window.getController().setJasperPrint(deserialize);
                    window.getController().show();
                    window.setClosable(true);
                    window.setMaximizable(true);
                    window.setTitle("Печать документа");
                    window.setMinimizable(true);
                    StartPage.addWindow(window);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        StringConverter<Number> sumConverter = new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                String pattern = CellFormats.TWO_DECIMAL_PLACES;
                DecimalFormat formatter = new DecimalFormat(pattern);
                return formatter.format(object);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        };
        summa.textProperty().bindBidirectional(orderBody.getOrderBodyInfo().sumProperty(), sumConverter);
        sumWoNds.textProperty().bindBidirectional(orderBody.getOrderBodyInfo().sumWoNdsProperty(), sumConverter);
        weight.textProperty().bindBidirectional(orderBody.getOrderBodyInfo().weightProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                String pattern = CellFormats.THREE_DECIMAL_PLACES;
                DecimalFormat formatter = new DecimalFormat(pattern);
                return formatter.format(object);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        count.textProperty().bindBidirectional(orderBody.getOrderBodyInfo().rowCountProperty(), new NumberStringConverter());
        orderDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                orderDate.setStyle(null);
            }
        });
        buyer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ContragentFx>() {
            @Override
            public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
                buyer.setStyle(null);
            }
        });
        shipper.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ContragentFx>() {
            @Override
            public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
                shipper.setStyle(null);
            }
        });
        StringConverter<ContragentFx> stringConverter = new StringConverter<ContragentFx>() {
            @Override
            public String toString(ContragentFx object) {
                return object.getName();
            }

            @Override
            public ContragentFx fromString(String string) {
                return null;
            }
        };

        ObtainContragentRequest obtainContragentRequest = new ObtainContragentRequest();
        obtainContragentRequest.getContragentTypes().add(ContragentType.BUYER);
        obtainContragentRequest.getContragentTypes().add(ContragentType.SOURCE);
        obtainContragentRequest.setActive(true);
        ObtainContragentResponse contragents = contragentsClient.getContragents(obtainContragentRequest);

        ObservableList<ContragentFx> fxBuyers = FXCollections.observableArrayList();
        ObservableList<ContragentFx> fxSources = FXCollections.observableArrayList();
        for (ContragentDto contragentDto : contragents.getDataList()) {
            if (contragentDto.getContragentTypes().contains(ContragentType.BUYER)) {
                fxBuyers.add(ContragentHelper.getInstance().getFxEntity(contragentDto));
            }
            if (contragentDto.getContragentTypes().contains(ContragentType.SOURCE)) {
                fxSources.add(ContragentHelper.getInstance().getFxEntity(contragentDto));
            }
        }
        final Comparator<ContragentFx> comparator = new Comparator<ContragentFx>() {
            @Override
            public int compare(ContragentFx o1, ContragentFx o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };

        Collections.sort(fxBuyers, comparator);
        Collections.sort(fxSources, comparator);
        shipper.setItems(fxSources);

        shipper.setConverter(stringConverter);
        buyer.setItems(fxBuyers);
        buyer.setConverter(stringConverter);

        shipper.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    createContragentForm(shipper);
                }
            }
        });

        buyer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    createContragentForm(buyer);
                }
            }
        });
        setPaid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                orderHeaderFx.setDocumentStatus(OrderStatus.PAID_FOR);

            }
        });
    }

    final Window[] windows = new Window[1];
    ChangeListener<ContragentFx> shipperListener = new ChangeListener<ContragentFx>() {
        @Override
        public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
            if (newValue != null) {

                for (ContragentFx fx : shipper.getItems()) {
                    if (fx.getGuid().equals(newValue.getGuid())) {
                        if (windows[0] != null) {
                            windows[0].setCloseRequest(true);
                        }
                        shipper.getSelectionModel().select(newValue);
                    }
                }

            }
        }
    };
    ChangeListener<ContragentFx> buyerListener = new ChangeListener<ContragentFx>() {
        @Override
        public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
            if (newValue != null) {

                for (ContragentFx fx : buyer.getItems()) {
                    if (fx.getGuid().equals(newValue.getGuid())) {
                        if (windows[0] != null) {
                            windows[0].setCloseRequest(true);
                        }
                        buyer.getSelectionModel().select(newValue);
                    }
                }

            }
        }
    };


    private void createContragentForm(ComboBox<ContragentFx> control) {
        ContragentsForm contragentsForm;
        ContragentType contragentType = null;
        if (control == shipper) {
            contragentType = ContragentType.SOURCE;
        } else if (control == buyer) {
            contragentType = ContragentType.BUYER;
        }

        contragentsForm = new ContragentsForm(true, contragentType);
        contragentsForm.setObtainMode(true);
        Window<ContragentInfoController, ContragentsForm> window = StartPage.openContent(contragentsForm, null, getPrimaryStage());
        window.setTitle("Выберите контрагента");
        window.setClosable(true);
        window.setMaximizable(false);
        window.setModal(true);

        windows[0] = window;
        if (control == shipper) {
            contragentsForm.obtainItemProperty().removeListener(buyerListener);
            contragentsForm.obtainItemProperty().addListener(shipperListener);
        } else if (control == buyer) {
            contragentsForm.obtainItemProperty().removeListener(shipperListener);
            contragentsForm.obtainItemProperty().addListener(buyerListener);
        }
        StartPage.addWindow(window);
    }

    public void setOrder(OrderHeaderDto order) {
        if (order.getGuid()==null){
            order.setStatus(OrderStatus.DRAFT);
            setPaid.setVisible(false);
        }else{
            if (order.getStatus()==OrderStatus.PAID_FOR){
                setPaid.setSelected(true);
                setPaid.setDisable(true);
            }
        }
        if (order.getStatus()!= OrderStatus.DRAFT){
            shipper.setDisable(true);
            buyer.setDisable(true);
            orderDate.setDisable(true);
            comment.setEditable(false);
            orderBody.setEditable(false);
        }
        orderHeaderFx = OrderHeaderHelper.getInstance().getFxEntity(order);

        if (orderHeaderFx.getGuid() != null) {
            orderBody.getOrderBodySource().addAll(orderHeaderFx.getOrderBody());
            for (ContragentFx contragentFx : buyer.getItems()) {
                if (contragentFx.getGuid().equals(orderHeaderFx.getRecipient().getGuid())) {
                    buyer.getSelectionModel().select(contragentFx);
                }
            }
            for (ContragentFx contragentFx : shipper.getItems()) {
                if (contragentFx.getGuid().equals(orderHeaderFx.getSource().getGuid())) {
                    shipper.getSelectionModel().select(contragentFx);
                }
            }
        }
        shipper.valueProperty().bindBidirectional(orderHeaderFx.sourceProperty());
        buyer.valueProperty().bindBidirectional(orderHeaderFx.recipientProperty());
        orderNumber.textProperty().bindBidirectional(orderHeaderFx.numberProperty());
        if (orderHeaderFx.getDateDocument() != null) {
            orderDate.setValue(orderHeaderFx.getDateDocument().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        comment.textProperty().bindBidirectional(orderHeaderFx.commentProperty());
        if (orderHeaderFx.getGuid() == null) {
            orderDate.setValue(LocalDate.now());
        }
        status.setText(order.getStatus().getTitle());
        registerControlsProperties(save, shipper.valueProperty(), buyer.valueProperty(), orderDate.valueProperty(),setPaid.selectedProperty(),
                comment.textProperty(), orderBody.orderBodyTableViewChangePropertyProperty());
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        saveResult(true);
    }

    @Override
    protected UpdateOrderResponse save() {
        orderHeaderFx.setUserGuid(UserContextHolder.getPermissionContextDataThreadLocal().getUser().getGuid());
        orderHeaderFx.setOrderBody(orderBody.getOrderBodySource());
        orderHeaderFx.setDateDocument(Date.from(orderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (setPaid.isSelected()){
            orderHeaderFx.setDocumentStatus(OrderStatus.PAID_FOR);
        }
        boolean error = orderHeaderFx.hasError();
        if (error) {
            orderBody.setToValidate(true);
            setError(orderDate, "dateDocument", orderHeaderFx);
            setError(shipper, "source", orderHeaderFx);
            setError(buyer, "recipient", orderHeaderFx);
            orderBody.checkError();
            return null;
        }
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();

        updateOrderRequest.getDataList().add(orderHeaderFx.getEntity());

        UpdateOrderResponse response = orderClient.updateOrders(updateOrderRequest);

        UpdateOrderResult updateOrderResult = response.getImportResults().get(0);
        orderHeaderFx.setGuid(updateOrderResult.getGuid());
        orderHeaderFx.setNumber(updateOrderResult.getOrderNumber());
        for (UpdateBodyResult result : updateOrderResult.getBodyResults()) {
            for (OrderBodyFx bodyFx : orderHeaderFx.getOrderBody()) {
                if (bodyFx.getTransportGuid().equals(result.getTransportGuid())) {
                    bodyFx.setGuid(result.getGuid());
                    break;
                }
            }
        }
        return response;
    }
}
