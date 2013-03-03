package net.coprg.coprg;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import net.coprg.coprg.OAuth.CodeExchangeException;
import net.coprg.coprg.OAuth.NoRefreshTokenException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

class GDrive {

    private static Drive service = null;
    private static List<File> fileList = null;

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
                        OAuth.getStoredCredentials()).setApplicationName("CoProgramming").build();
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

    public static void refreshFileList(final GDriveListener listener) {
        new SwingWorker<List<File>, Void>() {
            @Override
            protected List<File> doInBackground() {
                try {
                    return fileList = retrieveAllFiles(getService());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void done() {
                try {
                    if (get() != null) {
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

    public static void createFile(final String fileName,
            final GDriveListener listener) {
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                File body = new File();
                body.setTitle(fileName + " coprg");
                body.setMimeType("text/plain");

                InputStream fileContent = GDrive.class.getResourceAsStream(Config.TEMPLATE_LOCATION);
                InputStreamContent mediaContent = new InputStreamContent("text/plain", fileContent);
                try {
                    if (getService().files().insert(body,
                                mediaContent).setConvert(true).execute() != null) {
                        return true;
                    }
                } catch (IOException e) {
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


    public static String[] getFileTitles() {
        if (fileList == null || fileList.isEmpty()) {
            return null;
        }
        ArrayList<String> titleArray = new ArrayList<String>();
        for (File f : fileList) {
            titleArray.add(f.getTitle().substring(0,
                        f.getTitle().length()-6));
        }
        String[] result = new String[titleArray.size()];
        return titleArray.toArray(result);
    }

    public static void openFilePage(int index) {
        if (fileList == null) {
            refreshFileList(null);
        }
        File f = fileList.get(index);
        if (f != null) {
            try {
                Desktop.getDesktop().browse(
                        new URI(f.getAlternateLink()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<File> retrieveAllFiles(Drive service) throws IOException {
        List<File> result = new ArrayList<File>();
        Files.List request = service.files().list().setQ(
                "title contains 'coprg'");

        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null &&
                request.getPageToken().length() > 0);

        Iterator<File> iterator = result.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().getTitle().endsWith(" coprg")) {
                iterator.remove();
            }
        }
        return result;
    }
}
