package com.rockson.rest;


public interface Router {
	void use(Handle handle);
	void use(String path,Handle handle);
	void use(Middle middle);
	void use(String path,Middle middle);
	void param(String path,Middle middle);
}
