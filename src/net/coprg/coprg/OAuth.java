package net.coprg.coprg;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class OAuth {

    private static final String CLIENTSECRETS_LOCATION = Config.CLIENTSECRETS_LOCATION;
    private static final String CREDENTIALSTORE_LOCATION = Config.CREDENTIALSTORE_LOCATION;

    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/drive"
            );

    private static CredentialStore store = null;
    private static GoogleAuthorizationCodeFlow flow = null;

    /**
     * Exception thrown when an error occurred while retrieving credentials.
     */
    public static class GetCredentialsException extends Exception {
    }

    /**
     * Exception thrown when a code exchange has failed.
     */
    public static class CodeExchangeException extends GetCredentialsException {
    }

    /**
     * Exception thrown when no refresh token has been found.
     */
    public static class NoRefreshTokenException extends GetCredentialsException {
    }

    /**
     * Retrieve the authorization URL.
     *
     * @return Authorization URL to redirect the user to.
     * @throws IOException Unable to load client_secrets.json.
     */
    public static String getAuthorizationUrl() throws IOException {
        GoogleAuthorizationCodeRequestUrl urlBuilder =
            getFlow().newAuthorizationUrl().setRedirectUri(REDIRECT_URI);
        return urlBuilder.build();
    }

    /**
     * Create a credential store and store it as a static class attribute.
     *
     * @return CredentialStore instance.
     * @throws IOException Unable to create credential store.
     */
    private static CredentialStore getStore() throws IOException {
        if (store == null) {
            GsonFactory jsonFactory = new GsonFactory();
            File file = new File(CREDENTIALSTORE_LOCATION);
            store = new FileCredentialStore(file, jsonFactory);
        }
        return store;
    }

    /**
     * Retrieve stored credentials.
     *
     * @return Stored Credential if found, {@code null} otherwise.
     */
    public static Credential getStoredCredentials() {
        try {
            return getFlow().loadCredential("default");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete OAuth 2.0 credentials in the application's database.
     *
     * @throws IOException Unable to delete credentials.
     */
    public static void deleteCredentials() throws IOException {
        Credential cred = getStoredCredentials();
        if (cred != null) {
            getStore().delete("default", cred);
        }
    }

    /**
     * Build an authorization flow and store it as a static class attribute.
     *
     * @return GoogleAuthorizationCodeFlow instance.
     * @throws IOException Unable to load client_secrets.json.
     */
    private static GoogleAuthorizationCodeFlow getFlow() throws IOException {
        if (flow == null) {
            HttpTransport httpTransport = new NetHttpTransport();
            GsonFactory jsonFactory = new GsonFactory();
            GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory,
                        GDrive.class.getResourceAsStream(CLIENTSECRETS_LOCATION));
            flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
                .setAccessType("offline").setCredentialStore(getStore()).build();
        }
        return flow;
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @param authorizationCode Authorization code to exchange for OAuth 2.0
     *        credentials.
     * @return OAuth 2.0 credentials.
     * @throws CodeExchangeException An error occurred.
     */
    private static Credential exchangeCode(String authorizationCode)
        throws CodeExchangeException {
        try {
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response =
                flow.newTokenRequest(authorizationCode).setRedirectUri(REDIRECT_URI).execute();
            return flow.createAndStoreCredential(response, "default");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e);
            throw new CodeExchangeException();
        }
    }

    /**
     * Retrieve credentials using the provided authorization code.
     *
     * This function exchanges the authorization code for an access token. If a
     * refresh token has been retrieved along with an access token, it is stored
     * in the application database.
     *
     * @param authorizationCode Authorization code to use to retrieve an access
     *        token.
     * @return OAuth 2.0 credentials instance containing an access and refresh
     *         token.
     * @throws NoRefreshTokenException No refresh token could be retrieved from
     *         the available sources.
     */
    public static Credential getCredentials(String authorizationCode)
        throws CodeExchangeException, NoRefreshTokenException {
        try {
            Credential credentials = exchangeCode(authorizationCode);
            if (credentials.getRefreshToken() != null) {
                return credentials;
            }
        } catch (CodeExchangeException e) {
            e.printStackTrace();
            throw e;
        }
        throw new NoRefreshTokenException();
    }
}
