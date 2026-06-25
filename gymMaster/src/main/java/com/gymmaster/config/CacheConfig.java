package com.gymmaster.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Cache configuration backed by Redis.
 *
 * <p>Per-cache TTL strategy:
 * <ul>
 *   <li><b>facilities</b> — 10 minutes (rarely changes)</li>
 *   <li><b>coaches</b>    — 10 minutes</li>
 *   <li><b>courses</b>    — 5 minutes (updated more frequently)</li>
 *   <li><b>notices</b>    — 2 minutes (frequent updates)</li>
 *   <li>default           — 5 minutes</li>
 * </ul>
 */
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("facilities", defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigs.put("coaches",    defaultConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigs.put("courses",    defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("notices",    defaultConfig.entryTtl(Duration.ofMinutes(2)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
