package com.example.zemogatest.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zemogatest.ListStaticClass;
import com.example.zemogatest.models.Post;
import com.example.zemogatest.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    public ArrayList<Post> data;
    public static ArrayList<Post> normalData;

    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick(Post item, int pos);

    }

    public PostAdapter(ArrayList<Post> data, Context context, OnItemClickListener listener) {

        this.data = data;
        this.context = context;
        this.listener = listener;

        normalData = ListStaticClass.getInstance().getData();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_item, viewGroup, false);

        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder viewHolder, int pos) {

        Post item = data.get(pos);
        item.setActualPosition(pos);

        if(item.getPending() == null)
        {
            if (pos<21)
                item.setPending(true);
            else
                item.setPending(false);
        }

        if(item.getFavorite() == null)
            item.setFavorite(false);

        viewHolder.bindNews(item, context,listener, pos);

    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    public void clear() {
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class PostViewHolder
            extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private View dot, star;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        PostViewHolder(View itemView) {
            super(itemView);

            dot = itemView.findViewById(R.id.blue_dot);
            star = itemView.findViewById(R.id.star);
            txtTitle = itemView.findViewById(R.id.postTitleTv);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }

        void bindNews(final Post post, Context context, final OnItemClickListener listener, final int pos) {
            txtTitle.setText(post.getTitle().trim());
            if(post.getPending()) {
                dot.setVisibility(View.VISIBLE);
                dot.setBackground(ContextCompat.getDrawable(context, R.drawable.pending_dot));
            }
            else
                dot.setVisibility(View.GONE);
            if(post.getFavorite())
            {
                star.setVisibility(View.VISIBLE);
                star.setBackground(ContextCompat.getDrawable(context, android.R.drawable.star_big_on));
            }
            else
                star.setVisibility(View.GONE);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(post,pos);
                }
            });
        }
    }
}
