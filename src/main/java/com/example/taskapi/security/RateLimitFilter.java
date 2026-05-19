package com.example.taskapi.security;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter implements Filter {
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final RateLimitProperties rateLimitProperties;

    private static final Set<String> RATE_LIMITED_PATHS = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register"
    );

    private Bucket getBucket(String clientId) {
        int capacity = rateLimitProperties.getAuth().getCapacity();
        Duration interval = rateLimitProperties.getAuth().getInterval();
        return buckets.computeIfAbsent(clientId, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.builder()
                                .capacity(capacity)
                                .refillIntervally(capacity, interval)
                                .build())
                        .build()
        );
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!rateLimitProperties.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestPath = httpServletRequest.getRequestURI();

        if (!RATE_LIMITED_PATHS.contains(requestPath)) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = httpServletRequest.getRemoteAddr();
        Bucket bucket = getBucket(clientIp);
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        long retryAfterSeconds = Math.max(1, rateLimitProperties.getAuth().getInterval().getSeconds());
        httpServletResponse.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        httpServletResponse.setContentType("application/json");
        objectMapper.writeValue(
                httpServletResponse.getWriter(),
                ApiResponse.error(HttpStatus.TOO_MANY_REQUESTS.value(), "Too Many Requests")
        );
        httpServletResponse.getWriter().flush();
    }

}
