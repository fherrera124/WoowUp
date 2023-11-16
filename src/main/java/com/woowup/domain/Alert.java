package com.woowup.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class Alert {
    private final AlertTypeEnum type;
    private final User targetedUser;
    private final Topic topic;
    private final String content;
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

    //////////////// Constructors ////////////////

    public Alert(AlertTypeEnum type, Topic topic, String content) {
        this.type = type;
        this.topic = topic;
        this.content = content;
        this.targetedUser = null;
        // delegating to the topic itself
        // the notification of this alert
        topic.getNotified(this);
    }

    public Alert(AlertTypeEnum type, Topic topic, User user, String content) {
        this.type = type;
        this.topic = topic;
        this.content = content;
        this.targetedUser = user;
        // delegating to the topic itself
        // the notification of this alert
        topic.getNotified(this);
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
