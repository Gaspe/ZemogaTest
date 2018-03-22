package com.example.zemogatest.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.TinyDB;
import com.example.zemogatest.models.CallbackEvent;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.R;
import com.example.zemogatest.RecyclerItemTouchHelper;
import com.example.zemogatest.adapters.PostAdapter;
import com.example.zemogatest.models.PostCallbackEvent;
import com.example.zemogatest.requestImpl.ApiClient;
import com.example.zemogatest.requestImpl.ApiInterface;
import com.example.zemogatest.requestImpl.PostApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    ApiInterface apiService;
    ArrayList<Post> postList;
    PostAdapter adapter;
    ProgressBar bar;
    RecyclerView rv;
    RadioButton all;
    private LinearLayoutManager mLayoutManager;
    TinyDB tinydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinydb = new TinyDB(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        postList = new ArrayList<>();

        initializeViews();
        initializeListeners();

        FloatingActionButton fab = findViewById(R.id.delete_all_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.clear();
                ListStaticClass.getInstance().deleteData();
                Snackbar.make(view, "All posts deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initializeListeners() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b) {
                    ArrayList<Post> posts = new ArrayList<>();
                    for (Post p : ListStaticClass.getInstance().getData()) {
                        if (p.getFavorite() != null && p.getFavorite()) {
                            posts.add(p);
                        }
                    }
                    rv.setAdapter(new PostAdapter(posts, ListActivity.this, new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Post item, int pos) {
                            Intent in = new Intent(ListActivity.this, DetailActivity.class);
                            in.putExtra("position", pos);
                            in.putExtra("userId", item.getUserId());
                            startActivity(in);
                        }
                    }));
                }
                else
                {
                    rv.setAdapter(adapter);
                }
            }
        });

    }

    private void initializeViews() {

        all = findViewById(R.id.all_tab);

        bar =  findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(this);

        rv =  findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            loadNextDataFromApi();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadNextDataFromApi() {

        PostApi.getPosts();
    }

    public void initializeAdapter(ArrayList<Post> arrayList)
    {
        adapter = new PostAdapter(arrayList, ListActivity.this, new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post item, int pos) {
                Intent in = new Intent(ListActivity.this, DetailActivity.class);
                in.putExtra("position", pos);
                in.putExtra("userId", item.getUserId());
                adapter.data.get(pos).setPending(false);
                adapter.notifyItemChanged(pos);
                startActivity(in);;

            }
        });

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        adapter.removeItem(position);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);

        if(!ListStaticClass.getInstance().getIsUpdated())
        {
            ArrayList<Object> dbObjects = tinydb.getListObject("posts", Post.class);

            if (dbObjects != null && !dbObjects.isEmpty()) {
                postList.clear();

                for (Object objs : dbObjects) {
                    postList.add((Post) objs);
                }
                ListStaticClass.getInstance().setData(postList);
                initializeAdapter(postList);
                rv.setAdapter(adapter);
            } else {
                loadNextDataFromApi();
            }
        }
        else {
            if(adapter!=null)
                adapter.notifyDataSetChanged();
            else
                loadNextDataFromApi();
        }

        ListStaticClass.getInstance().setIsUpdated(false);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "I'm here");
    }

    @Subscribe
    public void onMessageEvent(PostCallbackEvent event)
    {
        if(event.isError())
            Toast.makeText(ListActivity.this, "Unable to retrieve the posts", Toast.LENGTH_SHORT).show();
        else
        {
            if(event.getType() == 0) {
                postList.clear();
                postList.addAll(event.getPosts());
                ArrayList<Post> posts = new ArrayList<>();
                posts.addAll(event.getPosts());
                ListStaticClass.getInstance().setData(posts);

                if (adapter != null) {
                    adapter.notifyItemRangeChanged(0, postList.size() - 1);

                } else {
                    initializeAdapter(postList);
                    rv.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        ArrayList<Object> dbObjects = new ArrayList<>();

        for(Post a : ListStaticClass.getInstance().getData()){
            dbObjects.add(a);
        }
        tinydb.putListObject("posts", dbObjects);

        EventBus.getDefault().unregister(this);
    }
}
