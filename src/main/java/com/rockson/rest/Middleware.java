package com.rockson.rest;

import java.util.regex.Pattern;

public class Middleware {
	public String method;
	public Pattern pathPattern;
	public Middle middle;

	public Middleware(String method, Pattern pathPattern, Middle middle) {
		this.method = method;
		this.pathPattern = pathPattern;
		this.middle = middle;
	}

	public boolean match(String method, String path) {
		if (null == this.method && null == this.pathPattern) {
			return true;
		}
		if (null != this.method && !this.method.equalsIgnoreCase(method)) {
			return false;
		}
		if (null != this.pathPattern && pathPattern.matcher(path).matches()) {
			return false;
		}
		return true;
	}

}
