package ru.metal.api.auth.dto;

import ru.metal.security.ejb.dto.AbstractDto;

import java.util.Date;

/**
 * Created by User on 12.09.2017.
 */
public class SessionDto extends AbstractDto {
    private Date startSession;

    private Date lastAction;

    private boolean closed;

    private User user;

    public Date getStartSession() {
        return startSession;
    }

    public void setStartSession(Date startSession) {
        this.startSession = startSession;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
