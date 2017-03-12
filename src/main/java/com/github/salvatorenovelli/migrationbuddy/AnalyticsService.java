package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class AnalyticsService {

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

    public GetReportsResponse run(String view_id, DateRange dateRange) throws Exception {

        Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");
        Metric newUser = new Metric().setExpression("ga:newUsers").setAlias("newUsers");

        Dimension browser = new Dimension().setName("ga:browser");
        Dimension landingPage = new Dimension().setName("ga:landingPagePath");

        return getReport(view_id, singletonList(dateRange), asList(sessions, newUser), singletonList(landingPage));
    }

    private GetReportsResponse getReport(String viewId, List<DateRange> dateRanges, List<Metric> metrics, List<Dimension> dimensions) throws IOException {

        ReportRequest request = new ReportRequest()
                .setViewId(viewId)
                .setDateRanges(dateRanges)
                .setDimensions(dimensions)
                .setMetrics(metrics);

        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(singletonList(request));

        return analyticsReporting.reports().batchGet(getReport).execute();
    }

    private AnalyticsReporting initializeAnalyticsReporting(Credential credential) throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new AnalyticsReporting.Builder(httpTransport, jsonFactory, credential).setApplicationName(Constants.APPLICATION_NAME).build();
    }

}
