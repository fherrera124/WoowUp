package com.woowup.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingStrategy {

    private static List<Alert> urgentAlerts = new ArrayList<>();
    private static List<Alert> informativeAlerts = new ArrayList<>();

    // static method
    public static List<Alert> sort(List<Alert> alerts) {

        urgentAlerts.clear();
        informativeAlerts.clear();

        List<Alert> result = null;

        // Separate alerts according to their type
        for (Alert alert : alerts) {
            if (AlertTypeEnum.URGENT.equals(alert.getType())) {
                urgentAlerts.add(alert);
            } else {
                informativeAlerts.add(alert);
            }
        }

        // urgent alerts ordered as LIFO
        Collections.reverse(urgentAlerts);

        // append the informative alerts after the urgent ones
        urgentAlerts.addAll(informativeAlerts);
        result = urgentAlerts;

        return result;

    }

}
