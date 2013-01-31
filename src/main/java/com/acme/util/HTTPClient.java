package com.acme.util;

import com.google.appengine.api.urlfetch.*;

import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.net.URL;

/**
 * A decorator to handle the HTTP traffic
 */
public class HTTPClient {

    private String accessToken;

    private String payload;

    private String mediaType;

    public String fetch(HTTPMethod method, String url) throws IOException {
        HTTPRequest request = new HTTPRequest(new URL(url), method, FetchOptions.Builder.withDeadline(20));
        request.setHeader(new HTTPHeader(HttpHeaders.ACCEPT, "application/json"));
        if (accessToken != null) {
            request.setHeader(new HTTPHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        }
        if (mediaType != null) {
            request.setHeader(new HTTPHeader(HttpHeaders.CONTENT_TYPE, mediaType));
        }
        if (payload != null) {
            request.setPayload(payload.getBytes("UTF-8"));
        }
        HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
        String buf = new String(response.getContent(), "UTF-8");

        if (response.getResponseCode() >= 400) {
            throw new RuntimeException("The server returned an error response: " +
                    "\nUrl: " + method + " " + url +
                    "\nStatus: " + response.getResponseCode() +
                    "\nResponse: " + buf);
        }
        return buf;
    }

    public HTTPClient withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public HTTPClient withPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public HTTPClient withMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }
}
