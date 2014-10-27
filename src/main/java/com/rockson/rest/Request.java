package com.rockson.rest;

import javax.servlet.http.HttpServletRequest;

public interface Request {
	String params(String name);
	String query(String name);
	String body(String name);
	String file(String name);
	String param(String name,Object... defaultValue);
	Route route(String name);
	String cookie(String name);
	String method();
	Cookie[] cookies();
	String signedCookies(String name);
	String get(String field);
	String accepts(String types);
	String acceptsCharsets(String charset,String... charsets);
	String acceptsLanguages(String lang ,String... langs);
	boolean is(String type);
	String ip();
	String[] ips();
	String path();
	String hostname();
	boolean fresh();
	boolean stale();
	boolean xhr();
	String protocol();
	boolean secure();
	String[] subdomains();
	String originalUrl();
	String baseUrl();
	HttpServletRequest req();

}
