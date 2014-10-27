package com.rockson.rest;

public interface Cookie {
	String getName();
	String setName();
	String getValue();
	String setValue();
	String setExpireAt(long ms);
	String getExpireAt();
	String setDomain(String domain);
	String getDomain();
	String setSecure(boolean secure);
	String getSecure();
	String setHttpOnly(boolean httpOnly);
	String getHttpOnly();
	String setMaxAge(long maxAge);
	String getMaxAge();
}
