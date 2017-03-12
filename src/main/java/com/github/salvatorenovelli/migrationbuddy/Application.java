package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {
        JsonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        Credential credentials = new GoogleAnalyticsCredentialFactory(new File(args[0]), jacksonFactory).authorize();
        AnalyticsService analyticsService = new AnalyticsService(credentials, jacksonFactory);
        analyticsService.init();

        DateRange dateRange = new DateRange();
        dateRange.setStartDate("30DaysAgo");
        dateRange.setEndDate("today");
        String view_id = "93074237";

        GetReportsResponse report = analyticsService.run(view_id, dateRange);
        new ReportPrinter(report).print();
    }
}
