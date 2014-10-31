package com.rockson.rest;

import java.util.Map;

public interface ViewRender {
	String render(String tpl, Map<String,?> data);
}
