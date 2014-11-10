package com.rockson.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface Request {
	String getParam(String name ,String... defaultValue);
	String param(String name);
	String query(String name);
	String body(String name);
	Map<String,String> body();
	FileField file(String name);
	List<String> getParams(String name);
	List<String> queries(String name);
	List<String> bodies(String name);
	List<FileField> files(String name);
	Route route(String name);
	Cookie cookie(String name);
	Cookie[] cookies();
	String method();
	String get(String field);
	String ip();
	List<String> ips();
	String path();
	String hostname();
	boolean xhr();
	String protocol();
	boolean secure();
	String url();
	String url(String url);
	String originalUrl();
	HttpServletRequest req();
	Map<String,List<FileField>> files();
	Map<String, List<String>> fields();
	Map<String, List<String>> queries();
	Map<String, String> params();
	Session session();
	void session(Session session);
	Map<Object , Object> var();
	Object var(Object key);
	void var(Object key,Object value);
	App app();
	void app(App app);
	
	void setParams(Map<String, String> params);
	void setQueries(Map<String, List<String>> queries);
	void setFields(Map<String, List<String>> fields);
	void setFiles(Map<String, List<FileField>> files);
	
	
}
