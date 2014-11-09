package com.rockson.rest;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface App extends Rest {
	
	void set(String name , String value);
	void get(String name);
	void enable(String name );
	boolean enabled(String name );
	void disable(String name );
	boolean disabled(String name );
	void use(Middle middle);
	void use(String path ,Middle middle);
	void use(Pattern path ,Middle middle);
	void use(String method,String path ,Middle middle);
	Map<String, Map<String, Handle>> routes();
	void listen();
	void path(String path );
	void mountPath(String path, App app);
	void onMount(OnMount callback);
	void handle(HttpServletRequest request,HttpServletResponse response);
	
	@FunctionalInterface
	public static interface OnMount {
		void apply(App parentApp);
	}

}
