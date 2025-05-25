package com.reciperestapi.reciperestapi.common.caching;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.concurrent.Callable;
public class CacheService {
    public <K, V> V getCache(Cache<K, V> cache, K key, Callable<V> dataLoader) throws Exception {
        V value = cache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        return loadAndCacheData(cache, key, dataLoader);
    }

    private <K, V> V loadAndCacheData(Cache<K, V> cache, K key, Callable<V> dataLoader) throws Exception {
        synchronized (this) {
            V value = cache.getIfPresent(key);
            if (value != null) {
                return value;
            }

            value = dataLoader.call();
            if (value != null) {
                cache.put(key, value);
            }
            return value;
        }
    }
}
