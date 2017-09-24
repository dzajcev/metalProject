package ru.metal.auth.impl.domain.persistent;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by User on 12.09.2017.
 */
@Entity
@Table(name = "SESSIONS")
public class Session extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_GUID",nullable = false)
    private UserData user;

    @Column(name = "DATE_START_SESSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startSession;

    @Column(name = "LAST__ACTION_OF_SESSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAction;

    @Column(name = "CLOSED")
    private boolean closed;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

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

}
