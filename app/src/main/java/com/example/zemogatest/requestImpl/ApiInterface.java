package com.example.zemogatest.requestImpl;

import com.example.zemogatest.models.Comment;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("posts")
    Call<ArrayList<Post>> getPosts();

    @GET("users/{user}")
    Call<User> getUser(@Path("user") int user);

    @GET("posts/{postId}/comments")
    Call<ArrayList<Comment>> getComments(@Path("postId") int postId);



}
