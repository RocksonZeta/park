package com.rockson.rest.utils;

@FunctionalInterface
public interface Fn2<P1,P2,R> {
	R apply(P1 p1, P2 p2);
}
