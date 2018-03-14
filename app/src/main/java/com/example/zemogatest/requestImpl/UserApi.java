package com.example.zemogatest.requestImpl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.activities.DetailActivity;
import com.example.zemogatest.activities.ListActivity;
import com.example.zemogatest.adapters.PostAdapter;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class UserApi
{
    private static ApiInterface apiService;
    private static Call<User> call;
    private static User user;

    public static User getUser(final Context context, int id)
    {

        call = apiService.getUser(id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                user = response.body();

            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                Toast.makeText(context, "Unable to retrieve the user", Toast.LENGTH_SHORT).show();
            }
        });

        return user;
    }
}
