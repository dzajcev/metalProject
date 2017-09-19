package ru.metal.gui.controllers.order;

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
import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.api.order.dto.UpdateBodyResult;
import ru.metal.api.order.dto.UpdateOrderResult;
import ru.metal.api.order.request.UpdateOrderRequest;
import ru.metal.api.order.response.UpdateOrderResponse;
import ru.metal.api.report.request.OrderReportRequest;
import ru.metal.api.report.response.OrderReportResponse;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.DocumentBodyFx;
import ru.metal.dto.OrderHeaderFx;
import ru.metal.dto.helper.ContragentHelper;
import ru.metal.dto.helper.OrderHeaderHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.AbstractController;
import ru.metal.gui.controllers.contragents.ContragentsForm;
import ru.metal.gui.controllers.print.JRViewerFxController;
import ru.metal.gui.windows.SaveMenuItem;
import ru.metal.gui.windows.Window;
import ru.metal.rest.ContragentsClient;
import ru.metal.rest.OrderClient;
import ru.metal.rest.ReportClient;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

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
                    // JasperViewerFX jasperViewerFX = new JasperViewerFX(deserialize);
                    // Window window = StartPage.openContent(jasperViewerFX, null, null);
                    // jasperViewerFX.show();
                    window.getController().setJasperPrint(deserialize);
                    window.getController().show();
                    window.setClosable(true);
                    window.setMaximizable(true);
                    window.setTitle("Печать документа");
                    window.setModal(true);
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
    }

    final Window[] windows = new Window[1];
    ChangeListener<ContragentFx> shipperListener = new ChangeListener<ContragentFx>() {
        @Override
        public void changed(ObservableValue<? extends ContragentFx> observable, ContragentFx oldValue, ContragentFx newValue) {
            if (newValue != null) {

                for (ContragentFx fx : shipper.getItems()) {
                    if (fx.getGuid().equals(newValue.getGuid())) {
                        if (windows[0] != null) {
                            windows[0].setHide(true);
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
                            windows[0].setHide(true);
                        }
                        buyer.getSelectionModel().select(newValue);
                    }
                }

            }
        }
    };

    private void createContragentForm(ComboBox<ContragentFx> control) {
        Window<AbstractController, ContragentsForm> window = StartPage.getWindow(ContragentsForm.ID);

        if (window != null && !window.getContent().isObtainMode()) {
            window.setCloseRequest(true);
            window = null;
        }
        ContragentsForm contragentsForm;
        ObtainContragentRequest request = new ObtainContragentRequest();
        if (control == shipper) {
            request.getContragentTypes().add(ContragentType.SOURCE);
        } else if (control == buyer) {
            request.getContragentTypes().add(ContragentType.BUYER);
        }
        if (window == null) {
            contragentsForm = new ContragentsForm(true, request);
            contragentsForm.setObtainMode(true);
            window = StartPage.openContent(contragentsForm, null, getPrimaryStage());
            window.setTitle("Контрагенты");
            window.setClosable(true);
            window.setMinimizable(false);
            window.setMaximizable(false);
            window.setModal(true);
            window.toFront();
        } else {
            contragentsForm = window.getContent();
        }
        windows[0] = window;

        if (control == shipper) {
            contragentsForm.obtainItemProperty().removeListener(buyerListener);
            contragentsForm.obtainItemProperty().addListener(shipperListener);
        } else if (control == buyer) {
            contragentsForm.obtainItemProperty().removeListener(shipperListener);
            contragentsForm.obtainItemProperty().addListener(buyerListener);
        }

        contragentsForm.setRequest(request);
        window.setModal(true);
        StartPage.addWindow(window);
    }

    public void setOrder(OrderHeaderDto response) {
        orderHeaderFx = OrderHeaderHelper.getInstance().getFxEntity(response);
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
        orderDate.valueProperty().bindBidirectional(orderHeaderFx.dateOrderProperty());
        comment.textProperty().bindBidirectional(orderHeaderFx.commentProperty());
        if (orderHeaderFx.getGuid()==null){
            orderDate.setValue(LocalDate.now());
        }
        registerControlsProperties(save, shipper.valueProperty(), buyer.valueProperty(), orderDate.valueProperty(),
                comment.textProperty(), orderBody.orderBodyTableViewChangePropertyProperty());
    }

    @FXML
    protected void saveAction(ActionEvent event) {
        saveResult(true);
    }

    @Override
    protected boolean save() {
        orderHeaderFx.setUserGuid(UserContextHolder.getPermissionContextDataThreadLocal().getUserGuid());
        orderHeaderFx.setOrderBody(orderBody.getOrderBodySource());

        boolean error = orderHeaderFx.hasError();
        if (error) {
            orderBody.setToValidate(true);
            setError(orderDate, "dateOrder", orderHeaderFx);
            setError(shipper, "source", orderHeaderFx);
            setError(buyer, "recipient", orderHeaderFx);
            orderBody.checkError();
            return false;
        }
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();

        updateOrderRequest.getDataList().add(orderHeaderFx.getEntity());
        UpdateOrderResponse response = null;

        response = orderClient.updateOrders(updateOrderRequest);

        UpdateOrderResult updateOrderResult = response.getImportResults().get(0);
        orderHeaderFx.setGuid(updateOrderResult.getGuid());
        orderHeaderFx.setNumber(updateOrderResult.getOrderNumber());

        for (UpdateBodyResult result : updateOrderResult.getBodyResults()) {
            for (DocumentBodyFx bodyFx : orderHeaderFx.getOrderBody()) {
                if (bodyFx.getTransportGuid().equals(result.getTransportGuid())) {
                    bodyFx.setGuid(result.getGuid());
                    break;
                }
            }
        }
        return true;
    }
}
