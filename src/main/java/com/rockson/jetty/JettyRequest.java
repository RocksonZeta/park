package com.rockson.jetty;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rockson.rest.Cookie;
import com.rockson.rest.Request;
import com.rockson.rest.Route;

public class JettyRequest implements Request {
	public final org.eclipse.jetty.server.Request baseRequest;
	public final HttpServletRequest req;
	public final String originPath;
	public JettyRequest(String path , org.eclipse.jetty.server.Request baseRequest,HttpServletRequest request ) {
		this.originPath = path;
		this.baseRequest = baseRequest;
		this.req = request;
	}
	public Map<String, String> params;

	@Override
	public String params(String name) {
		return null;
	}

	@Override
	public String query(String name) {
		return null;
	}

	@Override
	public String param(String name, Object... defaultValue) {
		return null;
	}

	@Override
	public Route route(String name) {
		return null;
	}

	@Override
	public String cookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] cookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String signedCookies(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String accepts(String types) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String acceptsCharsets(String charset, String... charsets) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String acceptsLanguages(String lang, String... langs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean is(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String ip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] ips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String path() {
		return this.originPath;
	}

	@Override
	public String hostname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fresh() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xhr() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String protocol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean secure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] subdomains() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String originalUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String baseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String file(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String method() {
		return req.getMethod();
	}

	@Override
	public String body(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpServletRequest req() {
		return this.req;
	}

}
