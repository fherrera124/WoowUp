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

    /*
     * An example of polymorphism: both Topic and User implement the
     * abstract method "resolveIncomingAlert(Alert)" as dictated by the abstract
     * class EntityAlertHandler.
     * Note that the Topic's implementation, internally calls the User's
     * implementation
     */
    public void resolveIncomingAlert(Alert alert) {

        if (alert.isTargeted()) {
            var targetedUser = alert.getTargetedUser();
            validateSubscription(targetedUser);
            // only the targeted subscriber get the alert
            targetedUser.resolveIncomingAlert(alert);
        } else {
            // all the subscribers get the alert
            for (var user : this.users) {
                user.resolveIncomingAlert(alert);
            }
        }
        // finally, if everything went well, the alert is
        // stacked in the queue of this topic
        alert.stackInQueue(alerts);
    }

    // add an observer to this topic
    public void addObserver(User user) {
        this.users.add(user);
    }

    private void validateSubscription(User targetedUser) {
        this.users.stream().filter(user -> user == targetedUser)
                .findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "El usuario " + targetedUser.getFullName() + " no está suscrito a este tema"));
    }

}
