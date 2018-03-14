package com.example.zemogatest.requestImpl;

import android.support.annotation.NonNull;

import com.example.zemogatest.models.CallbackEvent;
import com.example.zemogatest.models.Comment;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.models.PostCallbackEvent;
import com.example.zemogatest.models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.zemogatest.requestImpl.Config.apiService;

public class PostApi {

    private static Call<ArrayList<Post>> call;
    private static Call<ArrayList<Comment>> call2;

    public static void getPosts(){

        call = apiService.getPosts();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                EventBus.getDefault().post(new PostCallbackEvent(response.body(),false,0));
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {
                EventBus.getDefault().post(new PostCallbackEvent(null,true,0));
            }
        });

    }

    public static void getComments(int postId){

        call2 = apiService.getComments(postId);
        call2.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Comment>> call, @NonNull Response<ArrayList<Comment>> response) {
                EventBus.getDefault().post(new CallbackEvent(response.body(),false,1));
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Comment>> call, @NonNull Throwable t) {
                EventBus.getDefault().post(new CallbackEvent(null,true,1));
            }
        });

    }

}
