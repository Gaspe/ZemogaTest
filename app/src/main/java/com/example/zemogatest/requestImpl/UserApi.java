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
import com.example.zemogatest.models.CallbackEvent;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.models.User;
import com.example.zemogatest.models.UserCallbackEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

import static com.example.zemogatest.requestImpl.Config.apiService;

public class UserApi
{

    public static void getUser(final Context context, int id)
    {
        Call<User> call = apiService.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                EventBus.getDefault().post(new UserCallbackEvent(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                Toast.makeText(context, "Unable to retrieve the user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
