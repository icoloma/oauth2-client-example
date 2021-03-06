package com.acme.config;

/**
 * The OAuth endpoints and credentials.
 */
public class OAuthConfigImpl implements OAuthConfig {

    public static String HOSTNAME = System.getProperty("auth_hostname", "https://www.koliseo.com");

    private String authorizeUrl;

    private String accessTokenUrl;

    private String clientId;

    private String clientSecret;

    private String scope;

    public OAuthConfigImpl() {
        authorizeUrl = System.getProperty("auth_url", HOSTNAME + "/login/auth");
        accessTokenUrl = System.getProperty("auth_accesstoken_url", HOSTNAME + "/login/auth/token");
        clientId = System.getProperty("auth_client_id", "560004");
        clientSecret = System.getProperty("auth_client_secret", "37acf2f1-5be1-4b7c-91dc-f736714e2795");
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
