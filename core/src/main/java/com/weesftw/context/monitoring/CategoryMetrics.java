package com.weesftw.context.monitoring;

import com.weesftw.api.Utils;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Singleton
public class CategoryMetrics {

    private final MeterRegistry registry;
    private final Map<String, Long> allocationCount = new HashMap<>();

    public CategoryMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void execute(String args, boolean decrement) {
        var value = args.toLowerCase();
        long result = allocationCount.computeIfAbsent(value, s -> 0L);

        Gauge.builder("allocationCount", allocationCount, map -> map.get(value))
                .tag("category", Utils.capitalize(value))
                .description("Allocation count for each category")
                .register(registry);

        allocationCount.put(value, decrement ? result - 1L : result + 1L);
    }

    public Map<String, Long> allocationCount() {
        return allocationCount;
    }
}
