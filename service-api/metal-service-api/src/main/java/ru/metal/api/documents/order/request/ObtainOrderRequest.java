package ru.metal.api.documents.order.request;

import ru.common.api.request.ObtainAbstractRequest;

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

    private String userGuid;

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
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }
}
