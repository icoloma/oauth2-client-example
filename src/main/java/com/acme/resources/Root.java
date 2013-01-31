package com.acme.resources;

import com.acme.config.OAuthConfig;
import com.acme.config.OAuthConfigImpl;
import com.acme.model.ReceivedAccessToken;
import com.acme.model.UserData;
import com.acme.util.HTTPClient;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.view.Viewable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Resources of your application.
 * We are trying to keep things simple here:
 *
 * <ul>
 *     <li>Authenticate the user using the OAuth2 authorization provider</li>
 *     <li>Change the authorization code for an access token</li>
 *     <li>Store the access token as a cookie in the browser and in the datastore (this is probably NOT a good idea, but it's simple)</li>
 * </ul>
 */
@Path("/")
public class Root {

    public static final String USER_UUID = "user_uuid";

    @Inject
    private OAuthConfig config;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    UserRepository repository;

	@GET
	public Response index(@CookieParam(USER_UUID) String currentUser) throws UnsupportedEncodingException {
        if (currentUser != null) {
            return Response.seeOther(UriBuilder.fromPath("/events").build()).build();
        } else {
            String redirectUri = UriBuilder.fromPath("http://localhost:7777/callback").build().toString();
            UriBuilder authUri = UriBuilder.fromUri(config.getAuthorizeUrl())
                    .queryParam("response_type", "code")
                    .queryParam("state", "foobar")
                    .queryParam("client_id", config.getClientId())
                    .queryParam("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"));
            if (config.getScope() != null)
                authUri.queryParam("scope", config.getScope());
            return Response.ok().entity(new Viewable("/index.jsp", authUri.build().toString())).build();
        }
    }

    @GET @Path("callback")
    public Response callback(
            @QueryParam("error") String error,
            @QueryParam("code") String code,
            @QueryParam("state") String state
    ) throws IOException {

        if (error != null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Viewable("/unauthorized.jsp", error)).build();
        } else {
            Preconditions.checkArgument("foobar".equals(state));
            String s = new HTTPClient()
                    .withPayload(config.serializeAccessTokenArguments(state, code))
                    .fetch(HTTPMethod.POST, config.getAccessTokenUrl());
            ReceivedAccessToken token = objectMapper.readValue(s, ReceivedAccessToken.class);
            UserData userData = repository.put(fetchUserId(token.getAccessToken()), token.getAccessToken());
            URI uri = UriBuilder.fromPath("/events").build();
            return Response.seeOther(uri)
                    .cookie(new NewCookie(USER_UUID, userData.getUserUuid()))
                    .build();
        }
    }

    @GET @Path("logout")
    public Response logout() {
        return Response.seeOther(UriBuilder.fromPath("/").build())
                .cookie(new NewCookie(USER_UUID, "", "/", null, null, 0, false))
                .build();
    }

    private String fetchUserId(String accessToken) throws IOException {
        String s = new HTTPClient()
                .withAccessToken(accessToken)
                .fetch(HTTPMethod.GET, OAuthConfigImpl.HOSTNAME + "/users/current");
        Map<String, Object> user = objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {});
        String userId = (String) user.get("uuid");
        Preconditions.checkNotNull(userId);
        return userId;
    }

}
