package com.rockson.rest;

@FunctionalInterface
public interface Fn {
	Object apply(Object... args);
}
