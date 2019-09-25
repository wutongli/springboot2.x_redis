package cn.sz.lwt.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sz.lwt.pojo.Person;
import cn.sz.lwt.util.RedisUtil;


@RestController
@RequestMapping("/rc")
public class RedisController {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@RequestMapping("saveRedis")
	public Object saveRedis() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String value = "梧桐";
		RedisUtil redisUtil = new RedisUtil();
		redisUtil.setRedisTemplate(redisTemplate);
		Person p = new Person();
		p.setName(value);
		p.setAge(18);
		p.setBirthday(sdf.parse("2019-10-11 15:24:21"));
		//p.setBirthday(new Date());
		//redisUtil.set("w", value, 100l);
		//String res = (String) redisUtil.get("w");
		redisUtil.hPutIfAbsent("w", "person",p );
		Object res = redisUtil.hGet("w", "person");
		//redisUtil.delete("w");
		return res;
	}
}
