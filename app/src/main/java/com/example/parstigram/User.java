package com.example.parstigram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE = "profileImage";

    public String getKeyObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    public void setKeyObjectId(String objectId) {
        put(KEY_OBJECT_ID, objectId);
    }

    public String getKeyUsername() {
        return getString(KEY_USERNAME);
    }

    public void setKeyUsername(String username) {
        put(KEY_USERNAME, username);
    }

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setKeyProfileImage(ParseFile parseFile) {
        put(KEY_PROFILE_IMAGE, parseFile);
    }
}
