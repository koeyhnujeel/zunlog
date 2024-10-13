package zunza.zunlog.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import zunza.zunlog.Enum.CacheType
import java.util.*
import java.util.concurrent.TimeUnit

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(): CacheManager {
        val caches: List<CaffeineCache> = Arrays.stream(CacheType.entries.toTypedArray())
            .map { cache ->
                CaffeineCache(
                    cache.cacheName, Caffeine.newBuilder()
                        .expireAfterWrite(cache.expireAfterWrite, TimeUnit.MINUTES)
                        .maximumSize(cache.maximumSize)
                        .build()
                )
            }
            .toList()

        val cacheManager = SimpleCacheManager()
        cacheManager.setCaches(caches)

        return cacheManager
    }
}