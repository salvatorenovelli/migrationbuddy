package com.github.salvatorenovelli.migrationbuddy;

import com.github.salvatorenovelli.migrationbuddy.model.UrlStats;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.util.Map;

import static java.util.Collections.singletonList;

public class UrlStatsService {
    private final AnalyticsService analyticsService;

    public UrlStatsService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public Map<String, UrlStats> getUrlStats(String startDate, String endDate, String viewId) throws Exception {

        DateRange completeRange = new DateRange();
        completeRange.setStartDate(startDate);
        completeRange.setEndDate(endDate);

        GetReportsResponse report = analyticsService.run(viewId, singletonList(completeRange));
        ReportPrinter reportPrinter = new ReportPrinter(report);
        reportPrinter.print();

        return reportPrinter.getStatsPerUrl();
    }
}
