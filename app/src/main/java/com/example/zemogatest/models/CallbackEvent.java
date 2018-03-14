package com.example.zemogatest.models;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CallbackEvent {

    private User user;
    private ArrayList array;
    private boolean isError;

    public int getType() {
        return type;
    }

    private int type;

    public CallbackEvent(User user) {
        this.user = user;
    }

    public boolean isError() {
        return isError;
    }

    public CallbackEvent(ArrayList array, Boolean isError, int type) {
        this.array = array;
        this.isError = isError;
        this.type = type;

    }

    public User getUser() {
        return user;
    }

    public ArrayList<Post> getPosts() {
        return (ArrayList<Post>) array;
    }

    public ArrayList<Comment> getComments() {
        return (ArrayList<Comment>) array;
    }
}
