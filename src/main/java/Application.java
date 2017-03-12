import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {
        JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        Credential credentials = new GoogleAnalyticsCredentialFactory(new File(args[0]), jacksonFactory).authorize();
        AnalyticsService analyticsService = new AnalyticsService(credentials, jacksonFactory);
        analyticsService.init();
        GetReportsResponse report = analyticsService.run();
        new ReportPrinter(report).print();
    }
}
