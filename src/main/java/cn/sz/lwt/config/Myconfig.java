package cn.sz.lwt.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Configuration
//必须加，使配置生效,支持缓存注解
@EnableCaching
//继承CachingConfigurerSupport，为了自定义生成KEY的策略
public class Myconfig extends CachingConfigurerSupport{
	
	@Bean
	public ObjectMapper serializingObjectMapper() {
	  ObjectMapper objectMapper = new ObjectMapper();
	 
	  objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	  //设置jdk1.8时间
	  objectMapper.registerModule(new JavaTimeModule());
	  //设置可见度
	 objectMapper.setVisibility(PropertyAccessor.ALL,Visibility.ANY);
	  //全局开关，支持jackson在反序列是使用多态
	  objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL); //补上
	  return objectMapper;
	}
	
	//第一种方式
	 @Bean
	    public CacheManager cacheManager(RedisConnectionFactory factory) {
	        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
	        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
	        // 设置缓存的默认过期时间，也是使用Duration设置
	        config = config.entryTtl(Duration.ofMinutes(1))
	                .disableCachingNullValues();     // 不缓存空值

	        // 设置一个初始化的缓存空间set集合
	        Set<String> cacheNames =  new HashSet<>();
	        cacheNames.add("my-redis-cache1");
	        cacheNames.add("my-redis-cache2");

	        // 对每个缓存空间应用不同的配置
	        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
	        configMap.put("my-redis-cache1", config);
	        configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(120)));

	        // 使用自定义的缓存配置初始化一个cacheManager
	        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
	                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
	                .withInitialCacheConfigurations(configMap)
	                .build();
	        return cacheManager;
	    }
	//第二种方式
	/*@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)); // 设置缓存有效期一小时
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }*/
	
	
	 //序列化
	 	@Bean
	    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,ObjectMapper om) {

	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
	        redisTemplate.setConnectionFactory(redisConnectionFactory);
	        StringRedisSerializer stringSerializer = new StringRedisSerializer();
	        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(om);
	        redisTemplate.setKeySerializer(stringSerializer);//key序列化
	        redisTemplate.setValueSerializer(serializer);//value序列化
	        redisTemplate.setHashKeySerializer(stringSerializer);//Hash key序列化
	        redisTemplate.setHashValueSerializer(serializer);//Hash value序列化
	        redisTemplate.afterPropertiesSet();
	        return redisTemplate;

	    }
}
