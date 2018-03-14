package com.example.zemogatest.models;

/**
 * Created by Gaspar Villafane on 14/03/2018.
 */

public class UserCallbackEvent {

    private User user;

    public UserCallbackEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
