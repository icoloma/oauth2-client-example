package com.acme.resources;

import com.acme.config.OAuthConfig;
import com.acme.config.OAuthConfigImpl;
import com.acme.model.CursorList;
import com.acme.model.Event;
import com.acme.model.ReceivedAccessToken;
import com.acme.model.UserData;
import com.acme.util.HTTPClient;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
 *     <li>Ask for a protected resource using the access token</li>
 * </ul>
 */
@Path("/")
public class Root {

    private static final String USER_UUID = "user_uuid";

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

    @GET @Path("events")
    public Viewable events(
            @CookieParam(USER_UUID) String currentUserUuid,
            @InjectParam HttpServletRequest request
    ) throws IOException {
        Preconditions.checkArgument(currentUserUuid != null);
        UserData userData = repository.get(currentUserUuid);
        String s = new HTTPClient()
              .withAccessToken(userData.getAccessToken())
              .fetch(HTTPMethod.GET, OAuthConfigImpl.HOSTNAME + "/" + currentUserUuid + "/events");
        CursorList<Event> list = objectMapper.readValue(s, new TypeReference<CursorList<Event>>() {});
        request.setAttribute("userUuid", currentUserUuid);
        return new Viewable("/events.jsp", list);
    }

    @POST @Path(("create"))
    public Response createEvent(
            @CookieParam(USER_UUID) String currentUserUuid,
            @FormParam("eventName") String name
    ) throws IOException {
        Preconditions.checkArgument(name != null || name.length() > 0);
        UserData userData = repository.get(currentUserUuid);
        Event event = new Event();
        event.setName(name);
        String s = new HTTPClient()
                .withPayload(objectMapper.writeValueAsString(event))
                .withMediaType(MediaType.APPLICATION_JSON)
                .withAccessToken(userData.getAccessToken())
                .fetch(HTTPMethod.POST, OAuthConfigImpl.HOSTNAME + "/" + currentUserUuid);
        return Response.seeOther(UriBuilder.fromPath("/events").build()).build();
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
