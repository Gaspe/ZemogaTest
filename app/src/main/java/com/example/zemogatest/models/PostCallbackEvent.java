package com.example.zemogatest.models;

import java.util.ArrayList;


public class PostCallbackEvent {

    public PostCallbackEvent(ArrayList<Post> Posts, boolean isError, int type) {
        this.Posts = Posts;
        this.isError = isError;
        this.type = type;
    }

    public ArrayList getPosts() {
        return Posts;
    }

    public boolean isError() {
        return isError;
    }

    public int getType() {
        return type;
    }

    private ArrayList<Post> Posts;
    private boolean isError;
    private int type;

}
