package com.acme.resources;

import com.acme.model.AccessToken;
import com.google.appengine.api.datastore.*;

import javax.inject.Inject;

/**
 *
 */
public class AccessTokenRepository {

    private static final String KIND = "AccessToken";

    @Inject
    private DatastoreService datastore;

    public void put(String userId, AccessToken token) {
        Entity entity = new Entity(KIND, KeyFactory.createKey(KIND, userId));
        entity.setUnindexedProperty("token", token.getToken());
        datastore.put(entity);
    }

    public AccessToken get(String userId) {
        try {
            Entity entity = datastore.get(KeyFactory.createKey(KIND, userId));
            AccessToken token = new AccessToken();
            token.setToken((String) entity.getProperty("token"));
            return token;
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
