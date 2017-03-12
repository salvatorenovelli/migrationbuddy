import java.io.File;

public class Application {

    public static void main(String[] args) throws Exception {
        AnalyticsService analyticsService = new AnalyticsService(new File(args[0]));

        analyticsService.init();
        analyticsService.run();
    }
}
