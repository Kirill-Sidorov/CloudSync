package drive.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.File;
import drive.Auth;
import model.disk.Disk;
import model.disk.GoogleDisk;
import model.entity.Entity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Авторизация учетной записи облачного хранилища Google
 */
public class GoogleAuth implements Auth {
    /** Название приложения */
    private final String APPLICATION_NAME = "CloudSync";
    /** JSON фабрика */
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /** Список разрешений для учетной записи */
    private final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    /** Путь к учетным данным Google */
    private final String CREDENTIALS_FILE_PATH = "/credential/credentials.json";
    /** Путь к ключу от учетной записи */
    private final String tokenPath;

    public GoogleAuth(final String tokenPath) {
        this.tokenPath = tokenPath;
    }

    @Override
    public Disk authorize() {
        String name = "";
        Entity fileEntity = null;
        Drive service = null;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            About about = service.about().get().setFields("user").execute();
            File file = service.files().get("root").setFields("id, name, size, modifiedTime, mimeType, fileExtension").execute();
            String accountName = (about.getUser().getEmailAddress() != null) ? about.getUser().getEmailAddress() : "";
            name = "Google-" + accountName;
            fileEntity = new GoogleFileEntity(file).create();
        } catch (IOException e) {
            System.out.println("google credentials error");
        } catch (GeneralSecurityException e) {
            System.out.println("google authorize error");
        }
        return new GoogleDisk(name, fileEntity, service);
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleAuth.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}
