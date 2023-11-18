package com.woowup.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class Alert implements StackingStrategy<Alert> {
    @Getter
    private final User targetedUser;
    @Getter
    private final String content;
    @Getter
    private LocalDateTime expirationDate = null;
    private Map<User, Status> statuses = new HashMap<>();

    // this method must be called by an user when gets notified of an alert
    public void doneNotified(User user) {
        var status = new Status();
        statuses.put(user, status);
    }

    public void markAsReadForUser(User user) {
        var status = statuses.get(user);
        if (status == null) {
            throw new IllegalArgumentException("El usuario " + user.getFullName() + " no registra esta alerta");
        }
        status.markAsRead();
    }

    public Boolean wasReadByUser(User user) {
        var status = statuses.get(user);
        if (status == null) {
            throw new IllegalArgumentException("El usuario " + user.getFullName() + " no registra esta alerta");
        }
        return status.wasReadByUser();
    }

    public Boolean isTargeted() {
        return this.targetedUser != null;
    }

    public void setExpirationDate(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de expiración no puede ser anterior a la fecha actual");
        }
        this.expirationDate = date;
    }

    public void stackInQueue(List<Alert> alerts) {
        // any alert must know how to stack itself in the queue
        this.stackInQueue(alerts, this);
    }

    // constructor
    protected Alert(Topic topic, User user, String content) {
        this.content = content;
        this.targetedUser = user;
        // delegating to the topic itself
        // the notification of this alert
        topic.resolveIncomingAlert(this);
    }
    //////////////////////////////////////////////

    // Class that stores information and state of an alert and an User
    // In a database, it would be a "join table"
    // for now this class is private and belong to the alert
    @RequiredArgsConstructor
    private static class Status {
        // private final User user;
        // private final Alert alert;
        private Boolean read = false;
        @Getter
        private LocalDateTime readDateTime = null;

        public void markAsRead() {
            if (!read) {
                read = true;
                readDateTime = LocalDateTime.now();
                return;
            }
            // An IllegalStateException is a runtime exception in Java that is thrown to
            // indicate that a method has been invoked at the wrong time
            throw new IllegalStateException("La alerta ya fue leida");
        }

        public Boolean wasReadByUser() {
            return read;
        }
    }
}
