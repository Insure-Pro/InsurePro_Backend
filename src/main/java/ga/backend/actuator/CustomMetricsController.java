package ga.backend.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.Search;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

// 모든 api 사용현황 한 눈에 파악하기
@RestController
public class CustomMetricsController {

    private final MeterRegistry meterRegistry;

    public CustomMetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/actuator/api-usage")
    public Map<String, Object> getApiUsageMetrics() {
        // Retrieve all counters matching the name "api.usage.count"
        Search search = meterRegistry.find("api.usage.count");
        Map<String, Object> metrics = new HashMap<>();

        // Iterate over all matching counters
        search.counters().forEach(counter -> {
            String endpoint = counter.getId().getTag("endpoint");
            String method = counter.getId().getTag("method");
            double count = counter.count();

            // Build the response structure
            String key = "[" + method + "] " + endpoint;
            metrics.put(key, count);
        });

        return metrics;
    }
}
