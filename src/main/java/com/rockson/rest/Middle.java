package com.rockson.rest;

@FunctionalInterface
public interface Middle {
	boolean apply(Request req, Response res ,Next next);
}
