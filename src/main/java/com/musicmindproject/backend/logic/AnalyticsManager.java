package com.musicmindproject.backend.logic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.*;

/*
    Provides infos to our website
    fuck off buerger, comment!
 */
public class AnalyticsManager {
    private static AnalyticsManager instance;

    public static AnalyticsManager getInstance() {
        if (instance == null) {
            instance = new AnalyticsManager();
        }
        return instance;
    }

    private final String APPLICATION_NAME = "Public Website Statistics";
    private final String KEY_FILE_LOCATION = "/MusicMindProject-dd1f3e49e29b.json";
    private final String VIEW_ID = "164732604";
    private final String START_DATE = "2017-01-01";
    private final com.google.api.client.json.JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
    private long pageClicks;
    private long personalities;
    private long shares;
    private AnalyticsReporting service;

    private AnalyticsManager() {
        try {
            service = initializeAnalyticsReporting();
            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    AnalyticsManager.this.update();
                }
            }, 0L, 86400000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getPageClicks() {
        return pageClicks;
    }

    public long getGenerated() {
        return personalities;
    }

    public long getShares() {
        return shares;
    }

    private void update() {
        try {
            GetReportsResponse response = getGeneratedPersonalities(service);
            handleResponse(response);
            response = getShares(service);
            handleResponse(response);
            response = getTotalPageViews(service);
            handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AnalyticsReporting initializeAnalyticsReporting() throws java.security.GeneralSecurityException, IOException {
        com.google.api.client.http.HttpTransport httpTransport = com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport();
        com.google.api.client.googleapis.auth.oauth2.GoogleCredential credential = com.google.api.client.googleapis.auth.oauth2.GoogleCredential.fromStream(getClass().getClassLoader().getResourceAsStream("/MusicMindProject-dd1f3e49e29b.json")).createScoped(com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes.all());
        return new com.google.api.services.analyticsreporting.v4.AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName("Public Website Statistics").build();
    }

    private GetReportsResponse getTotalPageViews(AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("2017-01-01");
        dateRange.setEndDate("today");

        Metric pageviews = new Metric().setExpression("ga:pageviews").setAlias("pageviews");


        ReportRequest result = new ReportRequest().setViewId("164732604").setDateRanges(Arrays.asList(new DateRange[]{dateRange})).setMetrics(Arrays.asList(new Metric[]{pageviews}));

        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(Arrays.asList(new ReportRequest[]{result}));
        GetReportsResponse response = (GetReportsResponse) service.reports().batchGet(getReport).execute();
        return response;
    }

    private GetReportsResponse getGeneratedPersonalities(AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("2017-01-01");
        dateRange.setEndDate("today");

        Metric personalities = new Metric().setExpression("ga:totalEvents").setAlias("personalities");
        Dimension eventCategory = new Dimension().setName("ga:eventCategory");
        Dimension segmentDimensions = new Dimension().setName("ga:segment");

        Segment segment = getFilter("ga:eventCategory", "personality");


        ReportRequest result = new ReportRequest().setViewId("164732604").setDateRanges(Arrays.asList(new DateRange[]{dateRange})).setDimensions(Arrays.asList(new Dimension[]{eventCategory, segmentDimensions})).setSegments(Arrays.asList(new Segment[]{segment})).setMetrics(Arrays.asList(new Metric[]{personalities}));

        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(Arrays.asList(new ReportRequest[]{result}));
        GetReportsResponse response = (GetReportsResponse) service.reports().batchGet(getReport).execute();
        return response;
    }

    private GetReportsResponse getShares(AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("2017-01-01");
        dateRange.setEndDate("today");

        Metric personalities = new Metric().setExpression("ga:totalEvents").setAlias("shares");
        Dimension eventCategory = new Dimension().setName("ga:eventCategory");
        Dimension segmentDimensions = new Dimension().setName("ga:segment");

        Segment segment = getFilter("ga:eventCategory", "share");


        ReportRequest result = new ReportRequest().setViewId("164732604").setDateRanges(Arrays.asList(new DateRange[]{dateRange})).setDimensions(Arrays.asList(new Dimension[]{eventCategory, segmentDimensions})).setSegments(Arrays.asList(new Segment[]{segment})).setMetrics(Arrays.asList(new Metric[]{personalities}));

        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(Arrays.asList(new ReportRequest[]{result}));
        GetReportsResponse response = (GetReportsResponse) service.reports().batchGet(getReport).execute();
        return response;
    }

    private Segment getFilter(String category, String expression) {
        SegmentDimensionFilter dimensionFilter = new SegmentDimensionFilter().setDimensionName(category).setOperator("EXACT").setExpressions(Arrays.asList(new String[]{expression}));

        SegmentFilterClause segmentFilterClause = new SegmentFilterClause().setDimensionFilter(dimensionFilter);

        OrFiltersForSegment orFiltersForSegment = new OrFiltersForSegment().setSegmentFilterClauses(Arrays.asList(new SegmentFilterClause[]{segmentFilterClause}));

        SimpleSegment simpleSegment = new SimpleSegment().setOrFiltersForSegment(Arrays.asList(new OrFiltersForSegment[]{orFiltersForSegment}));

        SegmentFilter segmentFilter = new SegmentFilter().setSimpleSegment(simpleSegment);

        SegmentDefinition segmentDefinition = new SegmentDefinition().setSegmentFilters(Arrays.asList(new SegmentFilter[]{segmentFilter}));

        DynamicSegment dynamicSegment = new DynamicSegment().setSessionSegment(segmentDefinition).setName("Events with " + expression);

        return new Segment().setDynamicSegment(dynamicSegment);
    }

    private void handleResponse(GetReportsResponse response) {
        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();

            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows != null) {
                for (ReportRow row : rows) {
                    List<DateRangeValues> metrics = row.getMetrics();
                    for (int i = 0; i < metrics.size(); i++) {
                        DateRangeValues values = (DateRangeValues) metrics.get(i);
                        for (int k = 0; (k < values.getValues().size()) && (k < metricHeaders.size()); k++) {
                            if (((MetricHeaderEntry) metricHeaders.get(k)).getName().equals("pageviews")) {
                                pageClicks = Long.parseLong((String) values.getValues().get(k));
                            } else if (((MetricHeaderEntry) metricHeaders.get(k)).getName().equals("personalities")) {
                                personalities = Long.parseLong((String) values.getValues().get(k));
                            } else if (((MetricHeaderEntry) metricHeaders.get(k)).getName().equals("shares")) {
                                shares = Long.parseLong((String) values.getValues().get(k));
                            }
                        }
                    }
                }
            }
        }
    }
}
