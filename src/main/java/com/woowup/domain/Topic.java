package com.woowup.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// following the Observer pattern, this class acts as an Observable
@RequiredArgsConstructor
public class Topic extends EntityAlertHandler {
    @Getter
    private final String name;

    // following the Observer pattern, the users are the observers
    private Set<User> users = new HashSet<>();

    public void getNotified(Alert alert) {
        alerts.add(alert);

        var targetedUser = alert.getTargetedUser();
        if (targetedUser != null) {
            targetedUser.getNotified(alert);
        } else {
            // only the users who are subscribed to this topic get the alert
            for (var user : this.users) {
                user.getNotified(alert);
            }
        }

    }

    // subscribe an user to this topic
    public void subscribe(User user) {
        this.users.add(user);
    }

}
