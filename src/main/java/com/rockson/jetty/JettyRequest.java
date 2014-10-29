package com.rockson.jetty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.rockson.rest.FileField;
import com.rockson.rest.Request;
import com.rockson.rest.Route;
import com.rockson.rest.Session;

public class JettyRequest implements Request {
	public final org.eclipse.jetty.server.Request baseRequest;
	public final HttpServletRequest req;
	public final String originPath;
	protected Session session;
	public JettyRequest(String path , org.eclipse.jetty.server.Request baseRequest,HttpServletRequest request ) {
		this.originPath = path;
		this.baseRequest = baseRequest;
		this.req = request;
	}
	/**
	 * url params
	 */
	public final Map<String, String> params = new HashMap<>();
	
	/**
	 * query strings
	 */
	public final Map<String, List<String>> queries = new HashMap<>();
	/**
	 * post body
	 */
	public final Map<String, List<String>> fields = new HashMap<>();
	/**
	 * post files
	 */
	public final Map<String,FileField> files = new HashMap<>(); 
	

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
		return this.originPath;
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
		return this.originPath;
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
		// TODO Auto-generated method stub
		return null;
	}

}
