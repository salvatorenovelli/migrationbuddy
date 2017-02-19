
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.ApiDimensionFilter;
import com.google.api.services.webmasters.model.ApiDimensionFilterGroup;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class KeywordPositionChecker {

    private final String websiteURL;
    private final Webmasters apiClient;
    private final LocalDate start;
    private final LocalDate end;
    private final Locale locale;

    KeywordPositionChecker(String websiteURL, Webmasters apiClient, LocalDate start, LocalDate end, Locale locale) {
        this.websiteURL = websiteURL;
        this.apiClient = apiClient;
        this.start = start;
        this.end = end;
        this.locale = locale;
    }


    SearchAnalyticsQueryResponse checkKeyword(String query) {

        SearchAnalyticsQueryRequest queryRequest = new SearchAnalyticsQueryRequest();


        queryRequest
                .setStartDate(start.format(ISO_LOCAL_DATE))
                .setEndDate(end.format(ISO_LOCAL_DATE))
                .setDimensionFilterGroups(buildFilterGroups(query, locale))
                .setDimensions(Arrays.asList("date", "page", "country", "device", "query"));


        Webmasters.Searchanalytics searchAnalytics = apiClient.searchanalytics();


        try {
            Webmasters.Searchanalytics.Query query1 = searchAnalytics.query(websiteURL, queryRequest);
            SearchAnalyticsQueryResponse execute = query1.execute();
            return execute;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private List<ApiDimensionFilterGroup> buildFilterGroups(String query, Locale locale) {


        ApiDimensionFilterGroup filter = new ApiDimensionFilterGroup();
        filter.setFilters(Arrays.asList(
                new ApiDimensionFilter().setDimension("query").setOperator("equals").setExpression(query),
                new ApiDimensionFilter().setDimension("country").setExpression(locale.getISO3Country())
                //,new ApiDimensionFilter().setDimension("device").setExpression(deviceType.getName())
        ));


        return Collections.singletonList(filter);
    }

}