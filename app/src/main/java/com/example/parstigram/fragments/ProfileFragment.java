package com.example.parstigram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.parstigram.Post;
import com.example.parstigram.PostsAdapter;
import com.example.parstigram.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ProfileFragment extends PostsFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(() -> {
            Log.i(TAG, "Fetching new data!");
            queryPosts();
        });

        // Steps to use recycler view:
        // 0. Create layout for one row in the list
        // 1. Create the adapter
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        // 2. Create the data source
        // 3. Set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // 4. Set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }

            for (Post post : posts) {
                Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
            }

            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
        });
    }
}
