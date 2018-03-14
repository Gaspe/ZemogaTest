package com.example.zemogatest.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.R;
import com.example.zemogatest.adapters.CommentsAdapter;
import com.example.zemogatest.models.CallbackEvent;
import com.example.zemogatest.models.Comment;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.models.User;
import com.example.zemogatest.models.UserCallbackEvent;
import com.example.zemogatest.requestImpl.PostApi;
import com.example.zemogatest.requestImpl.UserApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ArrayList<Comment> comments;
    Button user;
    ListStaticClass staticClass;
    ListView listView;
    FloatingActionButton fab;
    Post currentItem;
    User currentUser;

    TextView body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        staticClass = ListStaticClass.getInstance();
        final int position = getIntent().getExtras().getInt("position");
        currentItem = staticClass.getData().get(position);
        fab =  findViewById(R.id.fab);

        setIcon();

        getSupportActionBar().setTitle(currentItem.getTitle());

        body = findViewById(R.id.body_tv);
        listView = findViewById(R.id.listView);
        user = findViewById(R.id.user_tv);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null)
                    displayAlert();
            }
        });

        body.setText(currentItem.getBody());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text;
                if(currentItem.getFavorite()) {

                    currentItem.setFavorite(false);
                    text = "Post removed from favorites!";
                }
                else
                {
                    currentItem.setFavorite(true);
                    text = "Post added to favorites!";
                }

                staticClass.setIsUpdated(true);
                setIcon();

                Snackbar.make(view, text , Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        UserApi.getUser(this, currentItem.getUserId());
        PostApi.getComments(currentItem.getId());
    }

    void setIcon()
    {
        if(currentItem.getFavorite())
            fab.setImageResource(android.R.drawable.star_big_on);
        else
            fab.setImageResource(android.R.drawable.star_big_off);
    }

    @Subscribe
    public void onEvent(CallbackEvent event) {

        if(event.getType() == 1) {

            if (!event.isError()) {
                comments = event.getComments();
                listView.setAdapter(new CommentsAdapter(this, comments));
            } else
                Toast.makeText(DetailActivity.this, "Unable to retrieve the comments", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onMessageEvent(UserCallbackEvent event) {
        if (event.getUser() != null) {
            currentUser = event.getUser();
            //user.setText(currentUser.getName());
        }
    }

    public void displayAlert()
    {
        String stringBuilder = "Name: " + currentUser.getName() + "\n" +
                "Username: " + currentUser.getUsername() + "\n" +
                "Email: " + currentUser.getEmail() + "\n" +
                "Phone: " + currentUser.getPhone() + "\n" +
                "Address: " + "\n\t" +
                "Street: " + currentUser.getAddress().getStreet() + "\n\t" +
                "Suite: " + currentUser.getAddress().getSuite() + "\n\t" +
                "City: " + currentUser.getAddress().getCity() + "\n\t" +
                "ZipCode: " + currentUser.getAddress().getZipcode() + "\n\t" +
                "Geo:" +"\n\t\t" +
                "Latitude: " + currentUser.getAddress().getGeo().getLat()+ "\n\t\t" +
                "Longitude: " + currentUser.getAddress().getGeo().getLng() + "\n" +
                "Website: " + currentUser.getWebsite() + "\n" +
                "Company: " + "\n\t" +
                "Name: " + currentUser.getCompany().getName() + "\n\t" +
                "CatchPhrase: " + currentUser.getCompany().getCatchPhrase() + "\n\t" +
                "Bs: " + currentUser.getCompany().getBs();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("User info")
                .setMessage(stringBuilder)
                .setNegativeButton(R.string.close_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
