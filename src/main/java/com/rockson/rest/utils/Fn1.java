package com.rockson.rest.utils;

@FunctionalInterface
public interface Fn1<P1,R> {
	R apply(P1 p1);
}
