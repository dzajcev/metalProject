package ru.metal.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.documents.order.dto.OrderHeaderDto;
import ru.metal.api.documents.order.dto.OrderStatus;
import ru.metal.dto.annotations.ValidatableCollection;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.OrderBodyHelper;
import ru.metal.dto.helper.OrderHeaderHelper;

import java.time.ZoneId;
import java.util.Date;

/**
 * Created by User on 30.08.2017.
 */
public class OrderHeaderFx extends FxEntity<OrderHeaderDto> implements DocumentHeader {

    @ValidatableField(nullable = false)
    protected ObjectProperty<Date> dateCreate = new SimpleObjectProperty<>(new Date());

    private StringProperty number = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private ObjectProperty<ContragentFx> source = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<OrderStatus> documentStatus = new SimpleObjectProperty<>(OrderStatus.DRAFT);

    @ValidatableField(nullable = false)
    private ObjectProperty<ContragentFx> recipient = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<Date> dateDocument = new SimpleObjectProperty<>();

    private StringProperty comment = new SimpleStringProperty();

    private StringProperty userGuid = new SimpleStringProperty();

    @ValidatableCollection(minSize = 1)
    private ObservableList<OrderBodyFx> orderBody = FXCollections.observableArrayList();

    public Date getDateCreate() {
        return dateCreate.get();
    }

    public ObjectProperty<Date> dateCreateProperty() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate.set(dateCreate);
    }

    @Override
    public String getNumber() {
        return number.get();
    }

    @Override
    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    @Override
    public ContragentFx getSource() {
        return source.get();
    }

    @Override
    public ObjectProperty<ContragentFx> sourceProperty() {
        return source;
    }

    public void setSource(ContragentFx source) {
        this.source.set(source);
    }

    @Override
    public ContragentFx getRecipient() {
        return recipient.get();
    }

    @Override
    public ObjectProperty<ContragentFx> recipientProperty() {
        return recipient;
    }

    public void setRecipient(ContragentFx recipient) {
        this.recipient.set(recipient);
    }

    @Override
    public Date getDateDocument() {
        return dateDocument.get();

    }

    @Override
    public ObjectProperty<Date> dateDocumentProperty() {
        return dateDocument;
    }

    public void setDateDocument(Date dateOrder) {
        this.dateDocument.set(dateOrder);

    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getUserGuid() {
        return userGuid.get();
    }

    public StringProperty userGuidProperty() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid.set(userGuid);
    }

    public OrderStatus getDocumentStatus() {
        return documentStatus.get();
    }

    public ObjectProperty<OrderStatus> documentStatusProperty() {
        return documentStatus;
    }

    public void setDocumentStatus(OrderStatus documentStatus) {
        this.documentStatus.set(documentStatus);
    }

    @Override
    public int countRows() {
        return orderBody.size();
    }

    @Override
    public IntegerProperty countRowsProperty() {
        return new SimpleIntegerProperty(orderBody.size());
    }

    @Override
    public double getSum() {
        double result = 0;
        for (OrderBodyFx bodyFx : orderBody) {
            result += (bodyFx.getPrice() * bodyFx.getCount());
        }
        return result;
    }

    @Override
    public DoubleProperty sumProperty() {
        return new SimpleDoubleProperty(getSum());
    }

    @Override
    public OrderHeaderDto getEntity() {
        OrderHeaderDto dto = new OrderHeaderDto();
        dto.setComment(getComment());
        dto.setCreateDate(getDateCreate());
        dto.setDateOrder(getDateDocument());
        dto.setNumber(getNumber());
        if (getRecipient() != null) {
            dto.setRecipient(getRecipient().getEntity());
        }
        if (getSource() != null) {
            dto.setSource(getSource().getEntity());
        }
        dto.setUserGuid(getUserGuid());
        dto.setGuid(getGuid());
        dto.setLastEditingDate(getLastEditingDate());
        dto.setTransportGuid(getTransportGuid());
        dto.setStatus(getDocumentStatus());
        dto.setBody(OrderBodyHelper.getInstance().getDtoCollection(getOrderBody()));
        return dto;
    }

    @Override
    public FxHelper<OrderHeaderFx, OrderHeaderDto> getHelper() {
        return OrderHeaderHelper.getInstance();
    }


    public ObservableList<OrderBodyFx> getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(ObservableList<OrderBodyFx> orderBody) {
        this.orderBody = orderBody;
    }
}
