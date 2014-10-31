package com.rockson.rest;

@FunctionalInterface
public interface Middle {
	void apply(Request req, Response res ,Next next);
}
