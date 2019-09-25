package cn.sz.lwt.util;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	//设置属性
	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return this.redisTemplate;
	}
	/** -------------------key相关操作--------------------- */

	/**
	 * 删除key
	 * 
	 * @param key
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	/**
	 * 批量删除key
	 * 
	 * @param keys
	 */
	public void delete(Collection<String> keys) {
		redisTemplate.delete(keys);
	}
	//给已存在key设置过期时间
	public  boolean expire(String key, long time) {

	        try {
	            if (time > 0) {
	                redisTemplate.expire(key, time, TimeUnit.SECONDS);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	//根据key获取时间,返回0代表为永久有效
	 public  long getExpire(String key) {

	        return redisTemplate.getExpire(key, TimeUnit.SECONDS);

	    }
	//判断key是否存在
	 public   boolean hasKey(String key) {

	        try {
	            return redisTemplate.hasKey(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 //获取String类型key
	 public  Object get(String key) {

	        return key == null ? null : redisTemplate.opsForValue().get(key);

	    }
	 //设置key并设置过期时间
	 public  boolean set(String key, Object value, long time) {

	        try {
	            if (time > 0) {
	                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	            } else {
	            	redisTemplate.opsForValue().set(key, value);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	 }
	 
	 /**
		 * 仅当hashKey不存在时才设置
		 * 
		 * @param key
		 * @param hashKey
		 * @param value
		 * @return
		 */
		public Boolean hPutIfAbsent(String key, String hashKey, Object value) {
			return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
		}
		/**
		 * 获取存储在哈希表中指定字段的值
		 * 
		 * @param key
		 * @param field
		 * @return
		 */
		public Object hGet(String key, String field) {
			return redisTemplate.opsForHash().get(key, field);
		}
}