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

    // Adds another filter from the one it already inherits from
    public List<Alert> getAlerts() {
        return super.getAlerts().stream().filter(alert -> !alert.wasReadByUser(this)).toList();
    }

    public void resolveIncomingAlert(Alert alert) {
        alert.stackInQueue(alerts);
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
        topic.addObserver(this);
    }

    public String getFullName() {
        return name + " " + surname;
    }
}