package com.woowup.domain;

import java.util.List;

import lombok.Builder;

public class UrgentAlert extends Alert {

    @Builder
    private UrgentAlert(Topic topic, User targetedUser, String content) {
        super(topic, targetedUser, content);
    }

    @Override
    public void stackInQueue(List<Alert> queue, Alert alert) {
        queue.add(0, alert);
    }
}
