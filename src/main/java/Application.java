import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;

import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {
        AnalyticsService analyticsService = new AnalyticsService(new File(args[0]));

        analyticsService.init();
        GetReportsResponse report = analyticsService.run();
        new ReportPrinter(report).print();
    }
}
