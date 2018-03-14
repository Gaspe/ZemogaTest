package com.example.zemogatest.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.R;
import com.example.zemogatest.models.Post;

public class DetailActivity extends AppCompatActivity {

    ListStaticClass staticClass;
    FloatingActionButton fab;
    Post currentItem;

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

        TextView body = findViewById(R.id.body_tv);
        body.setText(currentItem.getBody());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text;
                if(currentItem.getFavorite()) {

                    //staticClass.removeFavorite(currentItem);
                    currentItem.setFavorite(false);
                    text = "Post removed from favorites!";
                }
                else
                {
                    currentItem.setFavorite(true);
                    //staticClass.addFavorite(currentItem, position);
                    text = "Post added to favorites!";
                }

                setIcon();

                Snackbar.make(view, text , Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });


    }

    void setIcon()
    {
        if(currentItem.getFavorite())
            fab.setImageResource(android.R.drawable.star_big_on);
        else
            fab.setImageResource(android.R.drawable.star_big_off);

    }
}
