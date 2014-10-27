package com.rockson.rest;


public interface App extends Rest {
	
	void set(String name , String value);
	void get(String name);
	void enable(String name );
	void enabled(String name );
	void disable(String name );
	void disabled(String name );
	void use(Middle middle);
	void use(String path ,Middle middle);
	void use(String method,String path ,Middle middle);
	void engine(String path );
	void param(String path );
	void routing(String path );
	void route(String path );
	void locales(String path );
	void render(String path );
	void listen();
	void path(String path );
	void mountPath(String path );
	void onMount(String path );
	

}
