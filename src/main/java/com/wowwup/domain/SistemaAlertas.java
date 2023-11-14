package com.wowwup.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SistemaAlertas {
    @Getter
    private List<User> users;
    @Getter
    private List<Topic> topics;

    public SistemaAlertas() {
        this.users = new ArrayList<>();
        this.topics = new ArrayList<>();
    }

    public void registerUser(User newUser) {
        this.users.add(newUser);
    }

    public void registerTopic(Topic newTopic) {
        this.topics.add(newTopic);
    }

}
