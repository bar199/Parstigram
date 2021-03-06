package com.example.parstigram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("FVYcMzM5bmJGC9Rsb7nMXCGKl6eDRfEBrjSqwy4h")
                .clientKey("NWbGZvG0sMmhpZuFKRKrrsZqDNNxKNVcOZ9bbe0D")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
