package com.rockson.rest.utils;

@FunctionalInterface
public interface Fn4<P1,P2,P3,P4,R> {
	R apply(P1 p1, P2 p2,P3 p3,P4 p4);
}
