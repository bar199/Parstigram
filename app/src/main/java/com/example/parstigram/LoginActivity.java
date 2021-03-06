package com.example.parstigram;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;

/*
* A login screen that offers login via username/password.
*/
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //addShortcut();

        if(ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            Log.i(TAG, "onClick login button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });

        btnSignup.setOnClickListener(v -> {
            Log.i(TAG, "onClick signup button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            signupUser(username, password);
        });


    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);

        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                // TODO: better error handling
                Log.e(TAG, "Issue with login", e);
                displayToast("Issue with login!");
                return;
            }
            // Navigate to the main activity if the user has signed in properly
            goMainActivity();
        });
    }

    private void signupUser(String username, String password) {
        Log.i(TAG, "Attempting to signup user " + username);

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e != null) {
                switch (e.getCode()) {
                    case ParseException.USERNAME_TAKEN: {
                        Log.e(TAG, "Username already taken", e);
                        displayToast("Username already taken!");
                        break;
                    }

                    default: {
                        Log.e(TAG, "Issue with signup", e);
                        displayToast("Issue with signup!");
                    }
                }

                return;
            }

            // Navigate to the main activity if the user has signed in properly
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Custom toast
    private void displayToast(String message) {
        // Inflate toast XML layout
        View layout = getLayoutInflater().inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        // Fill in the message into the textview
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        // Construct the toast, set the view and display
        Toast toast = new Toast(getApplicationContext());
        toast.show();
    }

    @TargetApi(25)
    private void createShorcut() {
        ShortcutManager sM = getSystemService(ShortcutManager.class);

        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
        intent1.setAction(Intent.ACTION_VIEW);

        ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "shortcut1")
                .setIntent(intent1)
                .setShortLabel("Parstigram")
                .setLongLabel("Parstigram")
                .setDisabledMessage("Login to open this")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_parstigram))
                .build();

        sM.setDynamicShortcuts(Arrays.asList(shortcut1));
    }

    //TODO: Figure out how to add to homescreen
    /*
    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), LoginActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();

        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Parstigram");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_parstigram));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        addIntent.putExtra("duplicate" , false);
        getApplicationContext().sendBroadcast(addIntent);

    }

     */
}
