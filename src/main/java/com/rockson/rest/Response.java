package com.rockson.rest;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public interface Response {
	void status(int code);
	void set(String field , String value);
	String get(String field);
	void cookie(String name ,String value);
	void cookie(String name ,String value, int maxAge);
	void cookie(Cookie cookie);
	void clearCookie(String name);
	void redirect(String url);
	void redirect(int status, String url);
	void send(String body);
	void location(String location);
	void json(Object body);
	void jsonp(Object body);
	void jsonp(Object body,String method);
	void type(String type);
	HttpServletResponse res();
	void bufferSize(int size);
	void send(InputStream in);
	void send(Reader reader);
	void render(String tpl , Map<String, ?> data);
	void setViewRender(ViewRender render);
}
