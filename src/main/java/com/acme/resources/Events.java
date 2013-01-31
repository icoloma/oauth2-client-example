package com.acme.resources;

import com.acme.config.OAuthConfigImpl;
import com.acme.model.CursorList;
import com.acme.model.Event;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;

/**
 * This is an example of how to use the access token to invoke any API.
 * Shows your list of configured events in Koliseo, and may create new events on your behalf
 */
@Path("/events")
public class Events {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    UserRepository repository;

    @GET
    public Viewable events(
            @CookieParam(Root.USER_UUID) String currentUserUuid,
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

    @POST
    public Response createEvent(
            @CookieParam(Root.USER_UUID) String currentUserUuid,
            @FormParam("eventName") String name,
            @FormParam("background") String bg
    ) throws IOException {
        Preconditions.checkArgument(name != null || name.length() > 0);
        UserData userData = repository.get(currentUserUuid);
        Event event = new Event();
        event.setName(name);
        event.setBg(new Event.Background(bg));
        String s = new HTTPClient()
                .withPayload(objectMapper.writeValueAsString(event))
                .withMediaType(MediaType.APPLICATION_JSON)
                .withAccessToken(userData.getAccessToken())
                .fetch(HTTPMethod.POST, OAuthConfigImpl.HOSTNAME + "/" + currentUserUuid);
        return Response.seeOther(UriBuilder.fromPath("/events").build()).build();
    }

}
