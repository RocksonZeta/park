package com.rockson.jetty.middlewares;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rockson.rest.Middle;
import com.rockson.rest.Session;

public class RedisSession {
	protected String sessionKey ="park-sid" ;
	protected int maxAge = 24*3600;
	protected JedisPool jedisPool ;
	public RedisSession(String host, int port , Map<String, Object> conf ) {
		jedisPool = new JedisPool(host, port);
		if(null!= conf){
			if(conf.containsKey("sessionKey")){
				this.sessionKey = conf.get("sessionKey").toString();
			}
			if(conf.containsKey("maxAge")){
				this.maxAge = Integer.valueOf(conf.get("maxAge").toString());
			}
		}
	}
	
	public String genSessionId(){
		return UUID.randomUUID().toString();
	}
	
	public Middle apply() {
		return  (req ,res,next)->{
			Cookie sessionCookie = req.cookie(sessionKey);
			Jedis jedis = jedisPool.getResource();
			try{
				
				RedisSessionStore sessionStore = new RedisSessionStore();
				String sesssionId ;
				if(null == sessionCookie ){
					sesssionId = genSessionId();
					sessionCookie = new Cookie(sessionKey,sesssionId );
					jedis.setex(sesssionId, maxAge, sessionStore.encode());
				}else{
					sesssionId = sessionCookie.getValue();
					String data = jedis.get(sesssionId);
					if(null == data) {
						jedis.setex(sesssionId, maxAge, sessionStore.encode());
					}else{
						sessionStore.decode(data);
					}
				}
				sessionStore.sessionId = sesssionId;
				sessionCookie.setMaxAge(maxAge);
				sessionCookie.setPath("/");
				sessionCookie.setHttpOnly(true);
				req.session(sessionStore);
				res.cookie(sessionCookie);
				next.apply();
				jedis.setex(sesssionId, maxAge, sessionStore.encode());
			}finally{
				if(null!=jedis){
					jedisPool.returnResource(jedis);
				}
			}
		};
	}
	
	public class RedisSessionStore implements Session {
		Map<String, Object> data = new HashMap<>();
		String sessionId;
		
		public String encode(){
			return JSON.toJSONString(data, SerializerFeature.WriteClassName);
		}
		public void decode(String str) {
			this.data = JSON.parseObject(str, Map.class);
		}
		@Override
		public Object get(String key) {
			return data.get(key);
		}

		@Override
		public void set(String key, Object value) {
			data.put(key , value);
		}

		@Override
		public void destroy() {
			data = null;
		}

		@Override
		public boolean contains(String key) {
			return data.containsKey(key);
		}

		@Override
		public void save() {
			Jedis jedis = jedisPool.getResource();
			try{
				jedis.setex(sessionId, maxAge, this.encode());
			}finally{
				if(null!=jedis){
					jedisPool.returnResource(jedis);
				}
			}			
		}

		@Override
		public void reload() {
			Jedis jedis = jedisPool.getResource();
			try{
				decode(jedis.get(sessionId));
			}finally{
				if(null!=jedis){
					jedisPool.returnResource(jedis);
				}
			}	
		}
		
	}
}
