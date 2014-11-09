package com.rockson.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

public class ServletResponse implements Response {
	public final HttpServletResponse res;
	protected ViewRender viewRender; 
	
	public ServletResponse(HttpServletResponse response) {
		this.res = response;
	}
	
	@Override
	public void status(int code) {
		res.setStatus(code);
	}

	@Override
	public void set(String field, String value) {
		res.setHeader(field, value);
	}

	@Override
	public String get(String field) {
		return res.getHeader(field);
	}

	@Override
	public void cookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		this.cookie(cookie);
	}

	@Override
	public void cookie(String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		this.cookie(cookie);
	}

	@Override
	public void cookie(Cookie cookie) {
		res.addCookie(cookie);
	}

	@Override
	public void clearCookie(String name) {
		
	}

	@Override
	public void redirect(String url) {
		try {
			res.sendRedirect(url);
		} catch (IOException e) {
			throw new AppException(e);
		}
		
	}

	@Override
	public void redirect(int status, String url) {
		res.setStatus(status);
		this.redirect(url);
	}

	@Override
	public void location(String location) {
		res.setHeader("Location", location);
	}
	
	@Override
	public void send(String body) {
		try {
			this.res.getWriter().print(body);
			this.res.getWriter().flush();
		} catch (IOException e) {
			throw new AppException(e);
		}
		
	}

	@Override
	public void json(Object body) {
		this.type("application/json");
		this.send(JSON.toJSONString(body));
	}

	@Override
	public void jsonp(Object body) {
		this.send("callback("+JSON.toJSONString(body)+")");
	}
	
	@Override
	public void jsonp(Object body,String method) {
		this.send(method+"("+JSON.toJSONString(body)+")");
	}

	@Override
	public void type(String type) {
		res.setContentType(type);
	}


	@Override
	public HttpServletResponse res() {
		return this.res;
	}

	@Override
	public void bufferSize(int size) {
		res.setBufferSize(size);
	}

	@Override
	public void send(InputStream in) {
		try {
			IOUtils.copy(in ,res.getOutputStream());
		} catch (IOException e) {
			throw new AppException(e);
		}finally{
			try {
				if(null!=in)in.close();
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
	}

	@Override
	public void send(Reader reader) {
		try {
			IOUtils.copy(reader, res.getWriter());
		} catch (IOException e) {
			throw new AppException(e);
		}finally{
			try {
				if(null!=reader)reader.close();
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
	}
	
	@Override
	public void render(String tpl , Map<String, ?> data){
		this.send(this.viewRender.render(tpl, data));
	}
	
	@Override
	public void setViewRender(ViewRender render) {
		this.viewRender = render;
	}

	
}
