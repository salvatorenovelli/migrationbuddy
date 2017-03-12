package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

import static java.util.Collections.singletonList;


@SpringBootApplication
public class Application implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        if (strings.length > 0) {
            JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
            Credential credentials = new GoogleAnalyticsCredentialFactory(new File(strings[0]), jacksonFactory).authorize();
            AnalyticsService analyticsService = new AnalyticsService(credentials, jacksonFactory);
            analyticsService.init();

            String view_id = "93074237";

            int numOfDays = 30;

            DateRange completeRange = new DateRange();
            completeRange.setStartDate("2017-02-01");
            completeRange.setEndDate("2017-02-" + String.format("%02d", numOfDays));

            GetReportsResponse report = analyticsService.run(view_id, singletonList(completeRange));
            new ReportPrinter(report).print();
        } else {
            logger.info("Please provide client_secret.json");
        }
    }


}
