package ru.metal.gui.controllers.documents.order;

import javafx.collections.ObservableList;
import ru.metal.api.documents.order.dto.OrderStatus;
import ru.metal.dto.OrderHeaderFx;
import ru.metal.gui.controllers.documents.DocumentSelector;
import ru.metal.gui.controllers.documents.ObtainDocumentsRequest;

/**
 * Created by User on 23.09.2017.
 */
public class OrderDocumentSelector implements DocumentSelector<OrderHeaderFx, OrderStatus> {
    @Override
    public void dropDocument(OrderHeaderFx document) {

    }

    @Override
    public OrderStatus[] getDocumentStatusValues(){
        return OrderStatus.values();
    }
    @Override
    public OrderHeaderFx createEmptyDocument() {
        return null;
    }

    @Override
    public ObservableList<OrderHeaderFx> getDocuments(ObtainDocumentsRequest obtainDocumentsRequest) {
        return null;
    }

    @Override
    public String getId() {
        return "orderDocumentJournal";
    }
}
