package com.acme.config;

public interface OAuthConfig {

    String getAuthorizeUrl();

    String getAccessTokenUrl();

    String getClientId();

    String getClientSecret();

    String getScope();

}
