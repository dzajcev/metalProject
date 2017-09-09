package ru.metal.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.metal.api.order.dto.OrderHeaderDto;
import ru.metal.dto.annotations.ValidatableCollection;
import ru.metal.dto.annotations.ValidatableField;
import ru.metal.dto.helper.FxHelper;
import ru.metal.dto.helper.OrderBodyHelper;
import ru.metal.dto.helper.OrderHeaderHelper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by User on 30.08.2017.
 */
public class OrderHeaderFx extends FxEntity<OrderHeaderDto> {

    @ValidatableField(nullable = false)
    protected ObjectProperty<Date> createDate = new SimpleObjectProperty<>(new Date());

    private StringProperty number = new SimpleStringProperty();

    @ValidatableField(nullable = false)
    private ObjectProperty<ContragentFx> source = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<ContragentFx> recipient = new SimpleObjectProperty<>();

    @ValidatableField(nullable = false)
    private ObjectProperty<LocalDate> dateOrder = new SimpleObjectProperty<>();

    private StringProperty comment = new SimpleStringProperty();

    private StringProperty userGuid = new SimpleStringProperty();

    private BooleanProperty active=new SimpleBooleanProperty(true);

    @ValidatableCollection(minSize = 1)
    private ObservableList<DocumentBodyFx> orderBody= FXCollections.observableArrayList();

    public Date getCreateDate() {
        return createDate.get();
    }

    public ObjectProperty<Date> createDateProperty() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate.set(createDate);
    }

    public String getNumber() {
        return number.get();
    }

    public StringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public ContragentFx getSource() {
        return source.get();
    }

    public ObjectProperty<ContragentFx> sourceProperty() {
        return source;
    }

    public void setSource(ContragentFx source) {
        this.source.set(source);
    }

    public ContragentFx getRecipient() {
        return recipient.get();
    }

    public ObjectProperty<ContragentFx> recipientProperty() {
        return recipient;
    }

    public void setRecipient(ContragentFx recipient) {
        this.recipient.set(recipient);
    }

    public Date getDateOrder() {
        if (dateOrder.getValue()!=null) {
            Instant instantDateOrder = Instant.from(dateOrder.getValue().atStartOfDay(ZoneId.systemDefault()));
            return Date.from(instantDateOrder);
        }else{
            return null;
        }
    }

    public ObjectProperty<LocalDate> dateOrderProperty() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        if (dateOrder!=null) {
            this.dateOrder.set(dateOrder.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
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

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public OrderHeaderDto getEntity() {
        OrderHeaderDto dto=new OrderHeaderDto();
        dto.setComment(getComment());
        dto.setCreateDate(getCreateDate());
        dto.setDateOrder(getDateOrder());
        dto.setNumber(getNumber());
        if (getRecipient()!=null) {
            dto.setRecipient(getRecipient().getEntity());
        }
        if (getSource()!=null) {
            dto.setSource(getSource().getEntity());
        }
        dto.setUserGuid(getUserGuid());
        dto.setGuid(getGuid());
        dto.setLastEditingDate(getLastEditingDate());
        dto.setTransportGuid(getTransportGuid());
        dto.setActive(isActive());
        dto.setBody(OrderBodyHelper.getInstance().getDtoCollection(getOrderBody()));
        return dto;
    }

    @Override
    public FxHelper<OrderHeaderFx,OrderHeaderDto> getHelper() {
        return OrderHeaderHelper.getInstance();
    }


    public ObservableList<DocumentBodyFx> getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(ObservableList<DocumentBodyFx> orderBody) {
        this.orderBody = orderBody;
    }
}
