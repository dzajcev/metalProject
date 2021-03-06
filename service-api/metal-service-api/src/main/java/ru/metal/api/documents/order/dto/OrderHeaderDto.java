package ru.metal.api.documents.order.dto;

import ru.metal.security.ejb.dto.AbstractDto;
import ru.metal.api.contragents.dto.ContragentDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 30.08.2017.
 */
public class OrderHeaderDto extends AbstractDto {

    protected Date createDate = new Date();

    private String number;

    private ContragentDto source;

    private ContragentDto recipient;

    private Date dateOrder;

    private String comment;

    private String userGuid;

    private List<OrderBodyDto> body;

    private OrderStatus status;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContragentDto getSource() {
        return source;
    }

    public void setSource(ContragentDto source) {
        this.source = source;
    }

    public ContragentDto getRecipient() {
        return recipient;
    }

    public void setRecipient(ContragentDto recipient) {
        this.recipient = recipient;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderBodyDto> getBody() {
        if (body == null) {
            body = new ArrayList<>();
        }
        return body;
    }

    public void setBody(List<OrderBodyDto> body) {
        this.body = body;
    }
}
