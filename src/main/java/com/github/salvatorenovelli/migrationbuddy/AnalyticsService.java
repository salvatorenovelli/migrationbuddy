package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AnalyticsService {

    private static final String VIEW_ID = "93074237";
    private final Credential credential;
    private final JsonFactory jsonFactory;
    private AnalyticsReporting analyticsReporting;

    public AnalyticsService(Credential credential, JsonFactory jsonFactory) throws Exception {
        this.credential = credential;
        this.jsonFactory = jsonFactory;
    }

    public void init() throws Exception {
        analyticsReporting = initializeAnalyticsReporting(credential);
    }

    public GetReportsResponse run() throws Exception {
        return getReport(analyticsReporting);
    }

    private GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("30DaysAgo");
        dateRange.setEndDate("today");

        // Create the Metrics object.
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        Metric newUser = new Metric().setExpression("ga:newUsers").setAlias("newUsers");

        //Create the Dimensions object.
        Dimension browser = new Dimension().setName("ga:browser");
        Dimension landingPage = new Dimension().setName("ga:landingPagePath");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setDimensions(Arrays.asList(landingPage))
                .setMetrics(Arrays.asList(sessions, newUser));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    private AnalyticsReporting initializeAnalyticsReporting(Credential credential) throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new AnalyticsReporting.Builder(httpTransport, jsonFactory, credential).setApplicationName(Constants.APPLICATION_NAME).build();
    }

}
