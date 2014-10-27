package com.rockson.rest;

import java.util.regex.Pattern;

public interface Rest {
	
	void get(String path, Handle handle);
	void post(String path, Handle handle);
	void put(String path, Handle handle);
	void del(String path, Handle handle);
	void method(String method,String path, Handle handle);

	void get(Pattern regPath, Handle handle);
	void post(Pattern regPath, Handle handle);
	void put(Pattern regPath, Handle handle);
	void del(Pattern regPath, Handle handle);
	void method(String method,Pattern regPath, Handle handle);

	
}
