package com.rockson.rest;

import java.util.Map;
import java.util.regex.Pattern;

import com.rockson.rest.utils.Path;

public class PatternHandle {
	public String method;
	public Handle handle;
	public Pattern pattern;
	
	public PatternHandle() {
	}

	public PatternHandle(String method, Pattern pattern, Handle handle) {
		this.method = method;
		this.pattern = pattern;
		this.handle = handle;
	}

	public Map<String, String> match(String method,String path){
		if(null != this.method && !method.equalsIgnoreCase(this.method)){
			return null;
		}
		return Path.match(pattern, path);
	}
}
