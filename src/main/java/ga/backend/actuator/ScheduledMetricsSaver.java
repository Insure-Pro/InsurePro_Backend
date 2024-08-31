package ga.backend.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.Search;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledMetricsSaver {
    private final MeterRegistry meterRegistry;
    private final String filePath = "logs/api-usage/"; // 파일명 패턴 설정


    public ScheduledMetricsSaver(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

//    @Scheduled(cron = "0/5 * * * * ?")
    // 자정마다 파일 저장
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveMetricsToFile() {
        Map<String, Object> metricsData = collectMetrics();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "api-usage-metrics-" + timestamp + ".json"; // You can change to .csv if needed

        try (FileWriter fileWriter = new FileWriter(filePath + filename, true)) {
            fileWriter.write(metricsData.toString()); // Convert to JSON or format for CSV
            System.out.println("Metrics saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> collectMetrics() {
        Search search = meterRegistry.find("api.usage.count");
        Map<String, Object> metrics = new HashMap<>();

        search.counters().forEach(counter -> {
            String endpoint = counter.getId().getTag("endpoint");
            String method = counter.getId().getTag("method");
            double count = counter.count();
            String key = "[" + method + "] " + endpoint;
            metrics.put(key, count);
        });

        return metrics;
    }
}
