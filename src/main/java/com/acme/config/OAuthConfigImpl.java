package com.acme.config;

/**
 * The OAuth endpoints and credentials.
 */
public class OAuthConfigImpl implements OAuthConfig {

    public static String HOSTNAME = System.getProperty("auth_hostname", "https://sandbox.koliseo.com");

    private String authorizeUrl;

    private String accessTokenUrl;

    private String clientId;

    private String clientSecret;

    private String scope;

    public OAuthConfigImpl() {
        authorizeUrl = System.getProperty("auth_url", HOSTNAME + "/login/auth");
        accessTokenUrl = System.getProperty("auth_accesstoken_url", HOSTNAME + "/login/auth/token");
        clientId = System.getProperty("auth_client_id", "35");
        clientSecret = System.getProperty("auth_client_secret", "c86c0a7ab13d8e2dd1ae9c538053b951");
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String serializeAccessTokenArguments(String state, String authorizationCode) {
        return "grant_type=authorization_code" +
                "&code=" + authorizationCode +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                (state == null? "" : "&state=" + state);
    }

    public OAuthConfigImpl withScope(String scope) {
        this.scope = scope;
        return this;
    }

}
