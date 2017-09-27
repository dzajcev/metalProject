package ru.metal.gui.controllers.documents.order;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import ru.metal.api.documents.order.dto.OrderHeaderDto;
import ru.metal.api.documents.order.dto.OrderStatus;
import ru.metal.api.documents.order.request.DropOrderRequest;
import ru.metal.api.documents.order.request.ObtainOrderRequest;
import ru.metal.api.documents.order.response.ObtainOrderResponse;
import ru.metal.dto.ContragentFx;
import ru.metal.dto.OrderHeaderFx;
import ru.metal.dto.helper.OrderHeaderHelper;
import ru.metal.gui.StartPage;
import ru.metal.gui.controllers.documents.DocumentSelector;
import ru.metal.gui.controllers.documents.ObtainDocumentsRequest;
import ru.metal.gui.windows.Window;
import ru.metal.rest.OrderClient;

import java.util.List;

/**
 * Created by User on 23.09.2017.
 */
public class OrderDocumentSelector implements DocumentSelector<OrderHeaderFx, OrderStatus> {


    private OrderClient orderClient=new OrderClient();

    @Override
    public void openDocument(String documentGuid) {
        ObtainOrderRequest obtainOrderRequest=new ObtainOrderRequest();
        obtainOrderRequest.getGuids().add(documentGuid);
        ObtainOrderResponse orders = orderClient.getOrders(obtainOrderRequest);
        OrderHeaderDto orderHeaderDto = orders.getDataList().get(0);
        Window<OrderController, Node> window = StartPage.openContent("/fxml/Order.fxml", StartPage.primaryStage);
        if (window != null) {
            window.getController().setOrder(orderHeaderDto);
            window.setTitle("Счет на оплату");
            window.setClosable(true);
            window.setMinimizable(true);
            window.setModal(true);
            StartPage.addWindow(window);

        }
    }

    @Override
    public boolean dropDocument(OrderHeaderFx document) {
        DropOrderRequest dropOrderRequest=new DropOrderRequest();
        dropOrderRequest.getDataList().add(document.getGuid());
        orderClient.dropOrders(dropOrderRequest);
        return true;
    }

    @Override
    public OrderStatus[] getDocumentStatusValues(){
        return OrderStatus.values();
    }

    @Override
    public boolean isDroppable(OrderHeaderFx document) {
        return document.getDocumentStatus()==OrderStatus.DRAFT;
    }

    @Override
    public OrderHeaderFx createEmptyDocument() {
        return null;
    }

    @Override
    public ObservableList<OrderHeaderFx> getDocuments(ObtainDocumentsRequest<OrderStatus> obtainDocumentsRequest) {
        ObtainOrderRequest obtainOrderRequest=new ObtainOrderRequest();
        obtainOrderRequest.setEndDate(obtainDocumentsRequest.getEnd());
        obtainOrderRequest.setStartDate(obtainDocumentsRequest.getStart());
        obtainOrderRequest.setUserGuids(obtainDocumentsRequest.getUserGuids());

        for (ContragentFx contragentFx:obtainDocumentsRequest.getSources()){
            obtainOrderRequest.getSources().add(contragentFx.getGuid());
        }
        for (ContragentFx contragentFx:obtainDocumentsRequest.getRecipients()){
            obtainOrderRequest.getRecipients().add(contragentFx.getGuid());
        }
        obtainOrderRequest.getStatuses().addAll(obtainDocumentsRequest.getStatuses());
        ObtainOrderResponse orders = orderClient.getOrders(obtainOrderRequest);
        return OrderHeaderHelper.getInstance().getFxCollection(orders.getDataList());
    }

    @Override
    public String getId() {
        return "orderDocumentJournal";
    }
}
