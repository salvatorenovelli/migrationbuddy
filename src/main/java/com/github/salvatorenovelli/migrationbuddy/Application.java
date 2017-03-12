package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.io.File;

import static java.util.Collections.singletonList;

public class Application {

    public static void main(String[] args) throws Exception {
        JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        Credential credentials = new GoogleAnalyticsCredentialFactory(new File(args[0]), jacksonFactory).authorize();
        AnalyticsService analyticsService = new AnalyticsService(credentials, jacksonFactory);
        analyticsService.init();


        String view_id = "93074237";


        int numOfDays = 10;


        DateRange completeRange = new DateRange();
        completeRange.setStartDate("2017-02-01");
        completeRange.setEndDate("2017-02-" + String.format("%02d", numOfDays));

        GetReportsResponse report = analyticsService.run(view_id, singletonList(completeRange));
        new ReportPrinter(report).print();

        for (int i = 1; i <= 10; i++) {
            DateRange dateRange = new DateRange();
            dateRange.setStartDate("2017-02-" + String.format("%02d", i));
            dateRange.setEndDate("2017-02-" + String.format("%02d", i));
            GetReportsResponse report1 = analyticsService.run(view_id, singletonList(dateRange));
            new ReportPrinter(report1).print();
        }


    }
}
