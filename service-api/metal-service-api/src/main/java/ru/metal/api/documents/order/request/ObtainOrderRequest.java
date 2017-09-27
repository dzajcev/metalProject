package ru.metal.api.documents.order.request;

import ru.common.api.request.ObtainAbstractRequest;
import ru.metal.api.documents.order.dto.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 06.09.2017.
 */
public class ObtainOrderRequest extends ObtainAbstractRequest {
    private List<String> guids = new ArrayList<>();

    private List<String> numbers = new ArrayList<>();

    private Date startDate;

    private Date endDate;

    private List<String> recipients = new ArrayList<>();

    private List<String> sources = new ArrayList<>();

    private List<String> userGuids;

    private List<OrderStatus> statuses;

    public List<String> getGuids() {
        return guids;
    }

    public void setGuids(List<String> guids) {
        this.guids = guids;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getRecipients() {
        if (recipients==null){
            recipients=new ArrayList<>();
        }
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getSources() {
        if (sources==null){
            sources=new ArrayList<>();
        }
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getUserGuids() {
        if (userGuids == null) {
            userGuids = new ArrayList<>();
        }
        return userGuids;
    }

    public void setUserGuids(List<String> userGuids) {
        this.userGuids = userGuids;
    }

    public List<OrderStatus> getStatuses() {
        if (statuses == null) {
            statuses = new ArrayList<>();
        }
        return statuses;
    }

    public void setStatuses(List<OrderStatus> statuses) {
        this.statuses = statuses;
    }
}
