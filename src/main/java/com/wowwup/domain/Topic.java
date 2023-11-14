package com.wowwup.domain;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// Observable
// An observable is an object which notifies observers about the changes in its state.
@RequiredArgsConstructor
public class Topic {
    @Getter
    private final String name;

    // observers
    private List<User> users;

    public void addAlert(Alert alert) {
        // only the users whom are susbscribed to this topics got the alert
        for (var user : users) {
            user.addAlert(alert);
        }
    }

    // subscribe an user to this topic
    public void addObserver(User user) {
        this.users.add(user);
    }

}
