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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.TinyDB;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.R;
import com.example.zemogatest.RecyclerItemTouchHelper;
import com.example.zemogatest.adapters.PostAdapter;
import com.example.zemogatest.requestImpl.ApiClient;
import com.example.zemogatest.requestImpl.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String LIST_STATE_KEY = "list_key";
    ApiInterface apiService;
    ArrayList<Post> postList;
    Call<ArrayList<Post>> call;
    PostAdapter adapter;
    ProgressBar bar;
    RecyclerView rv;
    RadioButton all;
    private Parcelable mListState;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            loadNextDataFromApi(1,true);
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadNextDataFromApi(final int offset, final boolean isRefresh) {

        call = apiService.getPosts();

        if(!isRefresh)
            bar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                postList.clear();
                postList.addAll(response.body());
                ArrayList<Post> posts = new ArrayList<>();
                posts.addAll(response.body());
                ListStaticClass.getInstance().setData(posts);

                if (adapter != null) {
                    if (!isRefresh)
                        adapter.notifyItemRangeChanged(0, postList.size() - 1);
                    else {
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    initializeAdapter(postList);
                    rv.setAdapter(adapter);
                    bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {

                Toast.makeText(ListActivity.this, "Unable to retrieve the posts", Toast.LENGTH_SHORT).show();
            }
        });
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
                adapter.notifyDataSetChanged();
                startActivity(in);

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

        ArrayList<Object> dbObjects = tinydb.getListObject("posts", Post.class);
        if(dbObjects != null && !dbObjects.isEmpty()) {

            for(Object objs : dbObjects){
                postList.add((Post)objs);
            }
            ListStaticClass.getInstance().setData(postList);
            initializeAdapter(postList);
            rv.setAdapter(adapter);
        }
        else {
            loadNextDataFromApi(1, false);
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
    }
}
