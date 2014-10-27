package com.rockson.rest;


@FunctionalInterface
public interface Handle {
	void apply(Request req, Response res);
}
