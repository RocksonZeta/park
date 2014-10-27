package com.rockson.rest;

import javax.servlet.http.HttpServletResponse;

import com.rockson.rest.utils.Fn2;

public interface Response {
	void status(int code);
	void set(String field , String value);
	String get(int field);
	void cookie(String name ,String value);
	void cookie(String name ,String value,long expires);
	void cookie(Cookie cookie);
	void clearCookie(String name);
	void redirect(String url);
	void redirect(int status, String url);
	void send(String body);
	void location(String location);
	void json(Object body);
	void jsonp(Object body);
	void type(String type);
	void format(Fn2<Request,Response,String> fn );
	HttpServletResponse res();
}
