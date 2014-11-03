package com.rockson.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ServletRequest implements Request {
	
	public Session session;

	protected HttpServletRequest req;
	/**
	 * user defined varibles
	 */
	protected Map<Object ,Object> var = new HashMap<>();
	/**
	 * url params
	 */
	protected Map<String, String> params;
	
	/**
	 * query strings
	 */
	protected Map<String, List<String>> queries;
	/**
	 * post body
	 */
	protected Map<String, List<String>> fields;
	/**
	 * post files
	 */
	protected Map<String,List<FileField>> files; 
	
	public ServletRequest() {
	}
	
	public ServletRequest(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public String getParam(String name, String... defaultValue) {
		String value = params.get(name);
		if(null == value){
			value = this.query(name);
		}
		if(null ==value){
			value = this.body(name);
		}
		if(null == value && defaultValue.length>0){
			value = defaultValue[0];
		}
		return value;
	}
	
	@Override
	public String param(String name) {
		return params.get(name);
	}

	@Override
	public String query(String name) {
		return this.queries(name).get(0);
	}
	

	@Override
	public String body(String name) {
		return this.bodies(name).get(0);
	}
	
	@Override
	public Map<String,String> body() {
		Map<String, String> result = new HashMap<>(this.fields.size());
		for(Entry<String, List<String>> item : this.fields.entrySet()){
			result.put(item.getKey(), item.getValue().isEmpty()?null:item.getValue().get(0));
		}
		return result;
	}


	@Override
	public Route route(String name) {
		return null;
	}

	@Override
	public Cookie cookie(String name) {
		if(null == this.req.getCookies()){
			return null;
		}
		for(Cookie cookie: this.req.getCookies()){
			if(cookie.getName().equals(name)){
				return cookie;
			}
		}
		return null;
	}

	@Override
	public Cookie[] cookies() {
		return req.getCookies();
	}

	@Override
	public String signedCookies(String name) {
		return null;
	}

	@Override
	public String get(String field) {
		return req.getHeader(field);
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
		return req.getRemoteAddr();
	}

	@Override
	public String[] ips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String path() {
		return this.req.getRequestURI();
	}

	@Override
	public String hostname() {
		return req.getRemoteHost();
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
		return "XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"));
	}

	@Override
	public String protocol() {
		return this.req.getProtocol();
	}

	@Override
	public boolean secure() {
		return false;
	}

	@Override
	public String[] subdomains() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String originalUrl() {
		return this.req.getRequestURI();
	}

	@Override
	public String baseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileField file(String name) {
		return this.files(name).get(0);
	}

	@Override
	public String method() {
		return req.getMethod();
	}


	@Override
	public HttpServletRequest req() {
		return this.req;
	}

	@Override
	public Map<String, List<FileField>> files() {
		return this.files();
	}

	@Override
	public Map<String, List<String>> fields() {
		return this.fields;
	}

	@Override
	public Map<String, List<String>> queries() {
		return this.queries;
	}

	@Override
	public Map<String, String> params() {
		return this.params;
	}

	@Override
	public List<String> getParams(String name) {
		String param = this.param(name);
		if(null!=param){
			List<String> result = new ArrayList<String>(1);
			result.add(param);
			return result;
		}
		List<String> result  = null;
		result = queries(name);
		if(null!= result && !result.isEmpty()){
			return result;
		}
		result = bodies(name);
		if(null!= result && !result.isEmpty()){
			return result;
		}
		return null;
	}

	@Override
	public List<String> queries(String name) {
		return this.queries(name);
	}

	@Override
	public List<String> bodies(String name) {
		return this.fields.get(name);
	}

	@Override
	public List<FileField> files(String name) {
		return this.files(name);
	}

	@Override
	public Session session() {
		return this.session;
	}

	@Override
	public void session(Session session) {
		this.session = session;
	}

	@Override
	public Map<Object, Object> var() {
		return var;
	}

	@Override
	public Object var(Object key) {
		return var.get(key);
	}

	@Override
	public void var(Object key, Object value) {
		var.put(key, value);
	}

	@Override
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Override
	public void setQueries(Map<String, List<String>> queries) {
		this.queries = queries;
	}

	@Override
	public void setFields(Map<String, List<String>> fields) {
		this.fields = fields;
	}

	@Override
	public void setFiles(Map<String,List<FileField>> files) {
		this.files = files;
	}
	

}
