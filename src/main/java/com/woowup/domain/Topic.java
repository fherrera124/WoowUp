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
            validateSubscription(targetedUser);
            // only the targeted subscriber get the alert
            targetedUser.getNotified(alert);
        } else {
            // all the subscribers get the alert
            for (var user : this.users) {
                user.getNotified(alert);
            }
        }

    }

    // subscribe an user to this topic
    public void subscribe(User user) {
        this.users.add(user);
    }

    private void validateSubscription(User targetedUser) {
        this.users.stream().filter(user -> user == targetedUser)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "El usuario " + targetedUser.getFullName() + " no está suscrito a este tema"));
    }

}
