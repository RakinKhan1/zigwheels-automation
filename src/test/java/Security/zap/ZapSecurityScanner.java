package security.zap;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static security.config.SecurityConfig.MAX_HIGH_ALERTS;
import static security.config.SecurityConfig.MAX_MEDIUM_ALERTS;
import static security.config.SecurityConfig.ZAP_API_KEY;
import static security.config.SecurityConfig.ZAP_HOST;
import static security.config.SecurityConfig.ZAP_PORT;

public class ZapSecurityScanner {

    private final ClientApi api =
            new ClientApi(ZAP_HOST, ZAP_PORT, ZAP_API_KEY);

    public void checkConnection() throws ClientApiException {
        System.out.println("ZAP version: " + api.core.version());
    }

    public void waitForPassiveScanner() throws Exception {
        int remaining;
        do {
            Thread.sleep(2000);
            remaining = Integer.parseInt(
                    ((ApiResponseElement) api.pscan.recordsToScan()).getValue());
            System.out.println("Passive records remaining: " + remaining);
        } while (remaining > 0);
    }

    public ApiResponse alerts(String target) throws ClientApiException {
        return api.core.alerts(target, null, null);
    }

    public List<ApiResponse> alertItems(String target) throws ClientApiException {
        ApiResponse response = alerts(target);
        if (response instanceof ApiResponseList) {
            return ((ApiResponseList) response).getItems();
        }
        return List.of();
    }

    public Map<String, Integer> countByRisk(String target) throws ClientApiException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("High", 0);
        counts.put("Medium", 0);
        counts.put("Low", 0);
        counts.put("Informational", 0);

        for (ApiResponse item : alertItems(target)) {
            String risk = ((ApiResponseSet) item).getStringValue("risk");
            counts.merge(risk, 1, Integer::sum);
        }
        return counts;
    }

    public void printAlertSummary(String target) throws ClientApiException {
        Map<String, Integer> distinct = new LinkedHashMap<>();

        for (ApiResponse item : alertItems(target)) {
            ApiResponseSet set = (ApiResponseSet) item;
            String key = set.getStringValue("risk")
                    + " | " + set.getStringValue("alert");
            distinct.merge(key, 1, Integer::sum);
        }

        System.out.println("Distinct passive alerts for " + target);
        distinct.forEach((k, v) ->
                System.out.println("  " + k + "  (" + v + " instances)"));

        System.out.println("Counts by risk: " + countByRisk(target));
    }

    public void enforceQualityGate(String target) throws ClientApiException {
        Map<String, Integer> counts = countByRisk(target);
        int high = counts.getOrDefault("High", 0);
        int medium = counts.getOrDefault("Medium", 0);
        int low = counts.getOrDefault("Low", 0);

        System.out.println("Quality gate for " + target
                + ": High=" + high + " (max " + MAX_HIGH_ALERTS + "), "
                + "Medium=" + medium + " (max " + MAX_MEDIUM_ALERTS + ")");

        if (high > MAX_HIGH_ALERTS || medium > MAX_MEDIUM_ALERTS) {
            throw new AssertionError(
                    "Build rejected for " + target + ": ZAP found " + high + " high, "
                            + medium + " medium and " + low + " low alerts. Failing because "
                            + "high or medium exceeded the gate (max high " + MAX_HIGH_ALERTS
                            + ", max medium " + MAX_MEDIUM_ALERTS + ").");
        }
    }

    public void generateHtmlReport(String fileName) throws Exception {
        File dir = new File("reports").getAbsoluteFile();
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Could not create report folder: " + dir);
        }

        File reportFile = new File(dir, fileName);
        byte[] report = api.core.htmlreport();
        Files.write(reportFile.toPath(), report);

        System.out.println("Report written: " + reportFile.getAbsolutePath());
    }
}