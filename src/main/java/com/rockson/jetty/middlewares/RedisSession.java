package com.rockson.jetty.middlewares;

import com.rockson.rest.Middle;

public class RedisSession {
	
	
	
	public Middle apply() {
		return  (req ,res,next)->{
			next.apply();
		};
	}
}
