package com.rockson.rest;

public interface Session {
	Object get(String key);
	void set(String key , Object value);
	void destroy();
	void save();
	void reload();
}
