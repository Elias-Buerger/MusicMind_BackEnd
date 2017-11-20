package com.musicmindproject.backend.logic;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;

import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;

public class AnalyticsManager {

    private static AnalyticsManager instance;

    public static AnalyticsManager getInstance() {
        if(instance == null) {
            instance = new AnalyticsManager();
        }
        return instance;
    }

    private final String APPLICATION_NAME = "Public Website Statistics";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final String KEY_FILE_LOCATION = "MusicMindProject-dd1f3e49e29b.json";
    private final String VIEW_ID = "164732604";
    private int pageClicks;
    private int personalities;

    private AnalyticsManager() {
        try {
            AnalyticsReporting service = initializeAnalyticsReporting();
            GetReportsResponse response = getReport(service);
            handleResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPageClicks() {
        return pageClicks;
    }

    public int getGeneratedPersonalitites() {
        return personalities;
    }

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(getClass().getClassLoader().getResource(KEY_FILE_LOCATION).getFile())).createScoped(AnalyticsReportingScopes.all());

        return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("2017:11:20");
        dateRange.setEndDate("today");

        Metric pageviews = new Metric().setExpression("ga:pageviews").setAlias("pageviews");
        Metric personalities = new Metric().setExpression("ga:totalEvents").setAlias("events");
        Metric[] metrics = new Metric[] {pageviews, personalities};

        ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange)).setMetrics(Arrays.asList(metrics));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        return response;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response An Analytics Reporting API V4 response.
     */
    private void handleResponse(GetReportsResponse response) {
        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            for (ReportRow row : rows) {
                List<DateRangeValues> metrics = row.getMetrics();
                for (int i = 0; i < metrics.size(); i++) {
                    DateRangeValues values = metrics.get(i);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        if(metricHeaders.get(k).getName().equals("pageviews")) {
                            pageClicks = Integer.parseInt(values.getValues().get(k));
                        } else if(metricHeaders.get(k).getName().equals("events")) {
                            personalities = Integer.parseInt(values.getValues().get(k));
                        }
                    }
                }
            }
        }
    }
}
