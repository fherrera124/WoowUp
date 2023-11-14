package com.wowwup.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class User {
    private String name;
    private String surName;
    private List<Alert> receivedAlerts;

    public User(String name, String surName) {
        this.name = name;
        this.surName = surName;
        this.receivedAlerts = new ArrayList<>();
    }

    public void subscribeToTopic(Topic topic) {
        topic.addObserver(this);
    }

    public void addAlert(Alert alert) {
        this.receivedAlerts.add(alert);
    }
}