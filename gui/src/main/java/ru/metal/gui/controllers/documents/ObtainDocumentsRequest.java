package ru.metal.gui.controllers.documents;

import ru.metal.security.ejb.dto.User;
import ru.metal.api.documents.DocumentStatus;
import ru.metal.dto.ContragentFx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21.09.2017.
 */
public class ObtainDocumentsRequest {
    private List<ContragentFx> sources;
    private List<ContragentFx> recipients;
    private List<DocumentStatus> statuses;
    private List<String> userGuids;

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

    public List<DocumentStatus> getStatuses() {
        if (statuses==null){
            statuses=new ArrayList<>();
        }
        return statuses;
    }

    public void setStatuses(List<DocumentStatus> statuses) {
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
