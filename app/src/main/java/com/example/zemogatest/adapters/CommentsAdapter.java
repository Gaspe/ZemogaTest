package com.example.zemogatest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zemogatest.R;
import com.example.zemogatest.models.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CommentsAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<Comment> comments;

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        super(context,0,(List)comments);
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);

        TextView titleTv = convertView.findViewById(R.id.titleTv);
        TextView emailTv = convertView.findViewById(R.id.emailTv);
        TextView bodyTv = convertView.findViewById(R.id.bodyTv);
        titleTv.setText(comments.get(position).getName());
        emailTv.setText(comments.get(position).getEmail());
        bodyTv.setText(comments.get(position).getBody());

        return convertView;
    }

}
