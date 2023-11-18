package com.woowup.domain;

import java.util.List;

import lombok.Builder;

public class InformativeAlert extends Alert {

    @Builder
    private InformativeAlert(Topic topic, User targetedUser, String content) {
        super(topic, targetedUser, content);
    }

    @Override
    public void stackInQueue(List<Alert> queue, Alert alert) {
        queue.add(alert);
    }

}
