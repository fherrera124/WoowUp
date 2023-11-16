package com.woowup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityAlertHandler {
    protected List<Alert> alerts = new ArrayList<>();

    public List<Alert> getAlerts() {
        var now = LocalDateTime.now();
        return SortingStrategy.sort(alerts).stream()
                .filter(alert -> alert.getExpirationDate() == null ||
                        alert.getExpirationDate().isAfter(now))
                .toList();
    }

    // actions that the concrete class instance
    // must follow when receiving an alert
    public abstract void getNotified(Alert alert);
}
