package com.example.parstigram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parstigram.LoginActivity;
import com.example.parstigram.Post;
import com.example.parstigram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnPost;
    private Button btnLogout;
    private ProgressBar pbProgress;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public ComposeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnPost = view.findViewById(R.id.btnPost);
        btnLogout = view.findViewById(R.id.btnLogout);
        pbProgress = view.findViewById(R.id.pbProgress);

        btnCaptureImage.setOnClickListener(v -> {
            launchCamera();
        });

        //queryPosts();
        btnPost.setOnClickListener(v -> {
            String description = etDescription.getText().toString();

            if(description.isEmpty()) {
                displayToast("Description cannot be empty");
                return;
            }
            if(photoFile == null || ivPostImage.getDrawable() == null) {
                displayToast("There is no image!");
                return;
            }

            pbProgress.setVisibility(ProgressBar.VISIBLE);
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(description, currentUser, photoFile);
            pbProgress.setVisibility(ProgressBar.INVISIBLE);
        });

        btnLogout.setOnClickListener(v -> {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser == null) {
                goLoginActivity();
            } else {
                displayToast("Error logging out");
            }
        });
    }

    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                displayToast("Picture wasn't taken!");
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving", e);
                displayToast("Error while saving!");
            }

            Log.i(TAG, "Post was saved successfully!");
            etDescription.setText("");
            ivPostImage.setImageResource(0);
        });
    }

    private void displayToast(String message) {
        // Inflate toast XML layout
        View layout = getLayoutInflater().inflate(R.layout.toast,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

        // Fill in the message into the textview
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Construct the toast, set the view and display
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}