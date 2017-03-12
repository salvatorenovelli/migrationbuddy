import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analytics.AnalyticsScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class GoogleAnalyticsCredentialFactory {

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/" + Constants.APPLICATION_NAME);

    private final FileDataStoreFactory dataStoreFactory;
    private final HttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private GoogleClientSecrets clientSecrets;

    public GoogleAnalyticsCredentialFactory(File clientSecretJson, JsonFactory jsonFactory) throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
        this.jsonFactory = jsonFactory;
        this.clientSecrets = GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(new FileInputStream(clientSecretJson)));
    }

    public Credential authorize() throws Exception {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                jsonFactory,
                clientSecrets,
                Arrays.asList("https://www.googleapis.com/auth/webmasters.readonly", AnalyticsScopes.ANALYTICS_READONLY, AnalyticsScopes.ANALYTICS)
        ).setDataStoreFactory(dataStoreFactory).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}
