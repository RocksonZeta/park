package com.rockson.rest;

import java.util.regex.Pattern;

import com.rockson.rest.utils.Path;

public class Middleware {
	public String method;
	public String path;
	public Middle middle;

	public Middleware(String method, String path, Middle middle) {
		this.method = method;
		this.path = path;
		this.middle = middle;
	}

	public boolean match(String method, String path) {
		if (null == this.method && null == this.path) {
			return true;
		}
		if (null != this.method && !this.method.equalsIgnoreCase(method)) {
			return false;
		}
		if (null != this.path && Pattern.compile(Path.pathToReg(path)).matcher(path).matches()) {
			return false;
		}
		return true;
	}

}
