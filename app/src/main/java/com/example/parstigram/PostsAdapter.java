package com.example.parstigram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> postList) {
        posts.addAll(postList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //private ImageView ivProfileImage;
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvUserComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvUserComment = itemView.findViewById(R.id.tvUserComment);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvUsername.setText(post.getUser().getUsername());
            tvUserComment.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());

            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }

            //TODO: Figure out how to have profile image
            /*
            //Glide.with(context).load(post.getImage().getUrl()).transform(new RoundedCorners(100)).into(ivProfileImage);

            ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);

            //userQuery.whereEqualTo(User.KEY_OBJECT_ID, ParseObject.createWithoutData(User.class, post.getUser().toString()));
            User object = User.createWithoutData(User.class, post.getUser().getObjectId());
            userQuery.whereEqualTo(User.KEY_OBJECT_ID, object);

            //Go to the correct user's row and get the profile image

            userQuery.findInBackground((List<User> users, ParseException e) -> {
                if (e != null) {
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }

                for(User user : users) {
                    Log.i(TAG, "Post: " + post.getUser().toString() + ", profileImage: " + user.getProfileImage().getUrl());

                    ParseFile profileImage = user.getProfileImage();
                    if(profileImage != null) {
                        Glide.with(context).load(user.getProfileImage().getUrl()).transform(new RoundedCorners(100)).into(ivProfileImage);
                    }
                }
            });

            */
        }

    }
}
