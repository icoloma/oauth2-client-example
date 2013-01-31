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
        clientId = System.getProperty("auth_client_id", "19");
        clientSecret = System.getProperty("auth_client_secret", "a5ef448ae1be7a8514428ad53123b879");
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

    public OAuthConfigImpl withScope(String scope) {
        this.scope = scope;
        return this;
    }

}
