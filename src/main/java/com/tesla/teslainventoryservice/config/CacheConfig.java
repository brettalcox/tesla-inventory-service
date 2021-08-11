package com.tesla.teslainventoryservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@EnableCaching
@ConfigurationProperties(prefix = "cache")
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    private final Map<String, Long> names = new ConcurrentHashMap<>();

    @Bean
    RedisSerializer<Object> redisSerializer() {
        final ObjectMapper objectMapper = new ObjectMapper()
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    @Primary
    public CacheManager cacheManager(final CacheManager redisCacheManager) {
        final CompositeCacheManager compositeCacheManager = new CompositeCacheManager(redisCacheManager);
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }

    @Bean
    public CacheManager redisCacheManager(final RedisConnectionFactory redisConnectionFactory, final RedisSerializer<Object> redisSerializer) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .initialCacheNames(names.keySet())
                .withInitialCacheConfigurations(names.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.of(e.getValue(), ChronoUnit.SECONDS))
                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)))))
                .build();
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder ->  clientConfigurationBuilder.commandTimeout(Duration.of(5, ChronoUnit.SECONDS));
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            private final Logger LOGGER = LoggerFactory.getLogger(CacheErrorHandler.class);

            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
                LOGGER.error("Could not get cached value for key={}, return cache miss", o, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
                LOGGER.error("Could not cache value for key={}", o, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
                LOGGER.error("Could not evict cache value for key={}", o, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                LOGGER.error("Could not clear cache for cacheName={}", cache.getName(), e);
            }
        };
    }

    public Map<String, Long> getNames() {
        return names;
    }
}
