package net.coprg.coprg;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import net.coprg.coprg.OAuth.CodeExchangeException;
import net.coprg.coprg.OAuth.NoRefreshTokenException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;

class GDrive {

    private static Drive service = null;

    public static class GDriveListener {
        public void onSuccess() {
        }
        public void onFailure() {
        }
    }

    /**
     * Create a drive service and store it as a static class attribute.
     *
     * @return Drive instance.
     */
    public static Drive getService() {
        if (service == null) {
            Credential cred = OAuth.getStoredCredentials();
            if (cred != null) {
                service = new Drive.Builder(new NetHttpTransport(),
                        new GsonFactory(),
                        OAuth.getStoredCredentials()).build();
            }
        }
        return service;
    }

    /**
     * Authorize using the provided authorization code.
     *
     * @param authorizationCode Authorization code to use to retrieve an access
     *        token.
     */
    public static void authorize(final String authorizationCode,
            final GDriveListener listener) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    OAuth.getCredentials(authorizationCode);
                    return true;
                } catch (CodeExchangeException e) {
                    e.printStackTrace();
                } catch (NoRefreshTokenException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void done() {
                try {
                    if (get()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    listener.onFailure();
                }
            }
        }.execute();
    }

    public static void logout() {
        try {
            OAuth.deleteCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoggedIn() {
        return OAuth.getStoredCredentials() != null;
    }

    public static void openAuthorizePage() {
        try {
            Desktop.getDesktop().browse(
                    new URI(OAuth.getAuthorizationUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
