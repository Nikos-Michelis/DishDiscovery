package com.reciperestapi.reciperestapi.common.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;

@ApplicationScoped
@Data
public class CacheConfig {

    private final int CACHING_DURATION = 10;
    private final int MAX_ENTRIES = 1000;
    private final Cache<String, Object> cache;

    public CacheConfig() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(CACHING_DURATION, java.util.concurrent.TimeUnit.MINUTES)
                .maximumSize(MAX_ENTRIES)
                .build();
    }
}
