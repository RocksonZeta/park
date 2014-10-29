package com.rockson.jetty.middlewares;

import java.util.Map;

import com.rockson.rest.Middle;

public class LocalSession {
	private Map<String, Object> session;  
	
	public LocalSession() {
		
	}

	public Middle apply() {
		return  (req ,res,next)->{
			next.apply(null);
			return true;
		};
	}
}
