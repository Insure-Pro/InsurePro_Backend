package ga.backend.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiUsageFilter extends HttpFilter {
    private final MeterRegistry meterRegistry;

    public ApiUsageFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String endpoint = request.getRequestURI();
        String httpMethod = request.getMethod();

        // 상태 및 로그 레벨을 포함한 카운터 생성 및 증가
        Counter counter = meterRegistry.counter("api.usage.count",
                "endpoint", endpoint,
                "method", httpMethod);
        counter.increment(); // Increment the counter

        // 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }

}
