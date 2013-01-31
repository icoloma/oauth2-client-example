package com.acme.model;

import com.google.appengine.api.datastore.Key;

/**
 * User data persisted in the database.
 */
public class UserData {

    /** the uuid is primary key */
    private Key key;

    private String accessToken;

    public UserData() {
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserUuid() {
        return key.getName();
    }
}
