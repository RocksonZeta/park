package com.rockson.jetty.middlewares;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;

import com.rockson.rest.Middle;
import com.rockson.rest.Session;

public class LocalSession {
	Map<String,Object> conf;
	String sessionKey ="park-sid" ;
	int maxAge = 24*3600;
	final ConcurrentHashMap<String,SessionStore> sessionStores = new ConcurrentHashMap<>();
	Timer timer = new Timer(true);
	final int period = 60;

	public LocalSession() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Iterator<Entry<String, SessionStore>> iterator = sessionStores.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, SessionStore> entry = iterator.next();
					Cookie cookie = entry.getValue().cookie;
					if(cookie.getMaxAge()<=0){
						iterator.remove();
					}else{
						cookie.setMaxAge(cookie.getMaxAge()-period);
					}
				}
			}
		}, 0, period*1000);
	}
	public LocalSession(Map<String,Object> conf) {
		this();
		this.conf = conf;
		if(conf.containsKey("sessionKey")){
			this.sessionKey = conf.get("sessionKey").toString();
		}
		if(conf.containsKey("maxAge")){
			this.maxAge = Integer.valueOf(conf.get("maxAge").toString());
		}
	}

	public Middle apply() {
		return  (req ,res,next)->{
			Cookie reqCookie = req.cookie(sessionKey);
			if(null == reqCookie ){
				reqCookie = new Cookie(sessionKey, genSessionId());
				reqCookie.setMaxAge(maxAge);
				reqCookie.setHttpOnly(true);
				reqCookie.setPath("/");
				SessionStore sessionStore = new SessionStore(this,reqCookie);
				sessionStores.put(reqCookie.getValue(), sessionStore);
				req.session(sessionStore);
			}else{
				SessionStore sessionStore = sessionStores.get(reqCookie.getValue());
				if(null == sessionStore){
					sessionStore = new SessionStore(this,reqCookie);
					sessionStores.put(reqCookie.getValue(), sessionStore);
				}
				reqCookie.setMaxAge(maxAge);
				reqCookie.setPath("/");
				reqCookie.setHttpOnly(true);
				req.session(sessionStore);
			}
			res.cookie(reqCookie);
			next.apply();
		};
	}
	
	public String genSessionId(){
		return UUID.randomUUID().toString();
	}
	
	public class SessionStore implements Session {
		LocalSession localSession ;
		Cookie cookie ;
		Map<String, Object> data = new HashMap<>();
		public SessionStore(LocalSession localSession , Cookie cookie) {
			this.localSession = localSession;
			this.cookie = cookie;
		}

		@Override
		public Object get(String key) {
			return data.get(key);
		}

		@Override
		public void set(String key, Object value) {
			data.put(key, value);
		}

		@Override
		public void destroy() {
			data.clear();
			localSession.sessionStores.remove(this.cookie.getValue());
		}

		@Override
		public void save() {
			
		}

		@Override
		public void reload() {
			
		}

		@Override
		public boolean contains(String key) {
			return data.containsKey(key);
		}
		
	}

	
}
