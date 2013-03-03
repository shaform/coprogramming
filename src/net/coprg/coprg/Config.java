package net.coprg.coprg;

import java.io.File;
import java.util.ResourceBundle;

class Config {

    public static final String CLIENTSECRETS_LOCATION;
    public static final String LOCAL_DIRECTORY_LOCATION;
    public static final String CREDENTIALSTORE_LOCATION;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle(
                "net.coprg.coprg.config");

        CLIENTSECRETS_LOCATION = bundle.getString("CLIENTSECRETS_LOCATION");
        String localDirName = bundle.getString("LOCAL_DIRECTORY_NAME");
        File dir = new File(localDirName);
        if (!dir.isDirectory()) {
            localDirName = System.getProperty("user.home")
                + File.separator + localDirName;
            dir = new File(localDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
        LOCAL_DIRECTORY_LOCATION = localDirName;
        CREDENTIALSTORE_LOCATION = LOCAL_DIRECTORY_LOCATION
            + File.separator + bundle.getString("CREDENTIALSTORE_NAME");
    }
}
