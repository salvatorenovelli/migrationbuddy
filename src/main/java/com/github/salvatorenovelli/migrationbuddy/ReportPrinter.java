package com.github.salvatorenovelli.migrationbuddy;

import com.google.api.services.analyticsreporting.v4.model.*;

import java.util.List;

class ReportPrinter {
    private GetReportsResponse response;

    public ReportPrinter(GetReportsResponse response) {
        this.response = response;
    }

    public void print() {
        for (Report report : response.getReports()) {

            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for this view");
                return;
            }

            for (String dimensionHeader : dimensionHeaders) {
                System.out.print(dimensionHeader + " | ");
            }

            System.out.print(" --  | ");

            for (MetricHeaderEntry metricHeader : metricHeaders) {
                System.out.print(metricHeader.getName() + " | ");
            }

            System.out.println();

            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();

                System.out.println();

                for (int i = 0; i < dimensions.size(); i++) {
                    System.out.print(dimensions.get(i) + " | ");
                }

                System.out.print(" --  | ");


                for (int j = 0; j < metrics.size(); j++) {
                    //System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size(); k++) {
                        System.out.print(values.getValues().get(k) + " | ");
                    }
                }


            }
        }
    }
}
