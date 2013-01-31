package com.acme.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Access token data
 */
public class ReceivedAccessToken {

    /** Access token issued by the authorization server. */
    @JsonProperty("access_token")
    private String accessToken;

    /** Token type (as specified in <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-23#section-7.1">Access Token Types</a>). */
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    /** Lifetime in seconds of the access token */
    @JsonProperty("expires_in")
    private long expires;

    private String state;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
