package ru.metal.gui.controllers.documents;

import ru.metal.security.ejb.dto.User;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.dto.ContragentFx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 21.09.2017.
 */
public class ObtainDocumentsRequest<T extends DocumentStatus> {
    private Date start;
    private Date end;
    private List<ContragentFx> sources;
    private List<ContragentFx> recipients;
    private List<T> statuses;
    private List<String> userGuids;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<ContragentFx> getSources() {
        return sources;
    }

    public void setSources(List<ContragentFx> sources) {
        this.sources = sources;
    }

    public List<ContragentFx> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<ContragentFx> recipients) {
        this.recipients = recipients;
    }

    public List<T> getStatuses() {
        if (statuses==null){
            statuses=new ArrayList<>();
        }
        return statuses;
    }

    public void setStatuses(List<T> statuses) {
        this.statuses = statuses;
    }

    public List<String> getUserGuids() {
        if (userGuids==null){
            userGuids=new ArrayList<>();
        }
        return userGuids;
    }

    public void setUserGuids(List<String> userGuids) {
        this.userGuids = userGuids;
    }
}
