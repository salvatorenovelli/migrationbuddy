package com.github.salvatorenovelli.migrationbuddy;

import com.github.salvatorenovelli.migrationbuddy.model.ExcelInput;
import com.github.salvatorenovelli.migrationbuddy.model.UrlStats;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;


public class Application {


    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private AnalyticsService analyticsService;


    public Application(File clientSecretJson) throws Exception {
        JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        Credential credentials = new GoogleAnalyticsCredentialFactory(clientSecretJson, jacksonFactory).authorize();
        analyticsService = new AnalyticsService(credentials, jacksonFactory);
        analyticsService.init();
    }

    public static void main(String[] params) throws Exception {


        if (params.length > 0) {
            Application application = new Application(new File(params[0]));
            Map<String, UrlStats> urlStats = new UrlStatsService(application.analyticsService).getUrlStats("2017-02-01", "2017-02-28", "93074237");
            ExcelInput excelInput = new ExcelInput(params[1], urlStats);
            excelInput.populateSessionInformation();
            excelInput.save();
        } else {
            logger.info("Please provide client_secret.json");
        }
    }

}
