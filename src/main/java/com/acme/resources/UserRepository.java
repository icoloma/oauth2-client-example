package com.acme.resources;

import com.acme.model.UserData;
import com.google.appengine.api.datastore.*;

import javax.inject.Inject;

/**
 *
 */
public class UserRepository {

    @Inject
    private DatastoreService datastore;

    public UserData put(String userUuid, String accessToken) {
        Key key = KeyFactory.createKey(UserData.class.getSimpleName(), userUuid);
        Entity entity = new Entity(key);
        entity.setProperty("accessToken", accessToken);
        datastore.put(entity);

        UserData userData = new UserData();
        userData.setKey(key);
        userData.setAccessToken(accessToken);
        return userData;
    }

    public UserData get(String uuid) {
        try {
            Key key = KeyFactory.createKey(UserData.class.getSimpleName(), uuid);
            Entity entity = datastore.get(key);
            UserData instance = new UserData();
            instance.setKey(key);
            instance.setAccessToken((String) entity.getProperty("accessToken"));
            return instance;
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
