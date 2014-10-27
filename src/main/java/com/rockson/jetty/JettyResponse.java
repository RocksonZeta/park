package com.rockson.jetty;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.rockson.rest.AppException;
import com.rockson.rest.Cookie;
import com.rockson.rest.Request;
import com.rockson.rest.Response;
import com.rockson.rest.utils.Fn2;

public class JettyResponse implements Response {
	public final HttpServletResponse res;
	
	public JettyResponse(HttpServletResponse response) {
		this.res = response;
	}
	
	@Override
	public void status(int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(String field, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String get(int field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cookie(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookie(String name, String value, long expires) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCookie(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redirect(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redirect(int status, String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void location(String location) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void send(String body) {
		try {
			this.res.getWriter().print(body);
		} catch (IOException e) {
			throw new AppException(e);
		}
		
	}

	@Override
	public void json(Object body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jsonp(Object body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void type(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void format(Fn2<Request, Response, String> fn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HttpServletResponse res() {
		return this.res;
	}

	

}
