package com.github.salvatorenovelli.migrationbuddy;

import com.github.salvatorenovelli.migrationbuddy.model.UrlStats;
import com.google.api.services.analyticsreporting.v4.model.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class ReportPrinter {
    private GetReportsResponse response;

    public ReportPrinter(GetReportsResponse response) {
        this.response = response;
    }

    public Map<String, UrlStats> getStatsPerUrl() {

        return response.getReports().stream()
                .flatMap(report -> report.getData().getRows().stream())
                .map(this::toUrlstats)
                .collect(Collectors.toMap(UrlStats::getUrl, Function.identity()));

    }

    private UrlStats toUrlstats(ReportRow reportRow) {
        List<String> dimensions = reportRow.getDimensions();
        List<String> values = reportRow.getMetrics().get(0).getValues();
        return new UrlStats(dimensions.get(0), Long.valueOf(values.get(0)), Long.valueOf(values.get(1)));
    }

    public void print() {
        for (Report report : response.getReports()) {

            //System.out.println("REPORT1 FOR:" + report);

            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for this view");
                return;
            }

            for (String dimensionHeader : dimensionHeaders) {
                System.out.print(dimensionHeader + " , ");
            }

            for (MetricHeaderEntry metricHeader : metricHeaders) {
                System.out.print(metricHeader.getName() + " , ");
            }

            System.out.println();

            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                System.out.println();

                for (int i = 0; i < dimensions.size(); i++) {
                    System.out.print(dimensions.get(i) + " , ");
                }

                for (int j = 0; j < metrics.size(); j++) {
                    //System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size(); k++) {
                        System.out.print(values.getValues().get(k) + " , ");
                    }
                }


            }
        }
    }
}
