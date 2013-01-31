package com.acme.resources;

import com.acme.config.OAuthConfig;
import com.acme.config.OAuthConfigImpl;
import com.acme.model.AccessToken;
import com.acme.model.CursorList;
import com.acme.model.Event;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.view.Viewable;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Resources of your application.
 */
@Path("/")
public class Root {

    private static final String USER_ID_PARAM = "userId";

    @Inject
    private OAuthConfig config;

    @Inject
    private ObjectMapper objectMapper;

    @Inject AccessTokenRepository repository;

	@GET
	public Response index(@CookieParam(USER_ID_PARAM) String userId) throws UnsupportedEncodingException {
        if (userId != null) {
            return Response.seeOther(UriBuilder.fromPath("/events").build()).build();
        } else {
            String redirectUri = UriBuilder.fromPath("http://localhost:7777/callback").build().toString();
            UriBuilder authUri = UriBuilder.fromUri(config.getAuthorizeUrl())
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
            @QueryParam("code") String code,
            @QueryParam("state") String state
    ) throws IOException {
        Preconditions.checkArgument("foobar".equals(state));
        if (code == null) {
            throw new RuntimeException();
        } else {
            AccessToken token = fetch(config.getAccessTokenUrl(), new TypeReference<AccessToken>() {
            }, null);
            String userId = fetchUserId(token);
            repository.put(userId, token);
            URI uri = UriBuilder.fromPath("/events").build();
            return Response.seeOther(uri)
                    .cookie(new NewCookie(USER_ID_PARAM, userId))
                    .build();
        }
    }

    private String fetchUserId(AccessToken token) throws IOException {
        Map<String, Object> user = fetch(OAuthConfigImpl.HOSTNAME + "/currentUser", new TypeReference<Map<String, Object>>() {}, token);
        String userId = (String) user.get("uuid");
        Preconditions.checkNotNull(userId);
        return userId;
    }

    private <T> T fetch(String url, TypeReference type, AccessToken token) throws IOException {
        HTTPRequest request = new HTTPRequest(new URL(url));
        request.setHeader(new HTTPHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8"));
        if (token != null) {
            request.setHeader(new HTTPHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()));
        }
        HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
        return objectMapper.readValue(response.getContent(), type);
    }

    @GET @Path("events")
    public Viewable events(@CookieParam(USER_ID_PARAM) String userId) throws IOException {
        AccessToken token = repository.get(userId);
        CursorList<Event> list = fetch(OAuthConfigImpl.HOSTNAME + "/" + userId + "/events", new TypeReference<CursorList<Event>>() {}, token);
        return new Viewable("/events.jsp", list);
    }

}
