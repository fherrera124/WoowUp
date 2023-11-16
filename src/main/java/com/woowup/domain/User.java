package com.woowup.domain;

import java.util.List;

import lombok.Getter;

public class User extends EntityAlertHandler {
    @Getter
    private String name;
    @Getter
    private String surname;

    // Constructor
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    // 9. Se pueden obtener todas las alertas no expiradas de un usuario que aún no ha leído.
    public List<Alert> getAlerts() {
        return super.getAlerts().stream().filter(alert -> !alert.wasReadByUser(this)).toList();
    }

    public void getNotified(Alert alert) {
        alerts.add(alert);
        alert.doneNotified(this);
    }

    // We are delegating the action to the alert since the alert keeps track of the
    // state of itself with respect to the specified user
    public void markAlertAsRead(Alert alert) {
        alert.markAsReadForUser(this);
    }

    // We are delegating the action to the alert since the alert keeps track of the
    // state of itself with respect to the specified user
    public Boolean wasRead(Alert alert) {
        return alert.wasReadByUser(this);
    }

    public void subscribeToTopic(Topic topic) {
        topic.subscribe(this);
    }

    public String getFullName() {
        return name + " " + surname;
    }
}