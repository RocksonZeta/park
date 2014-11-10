package com.rockson.rest.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TestPath {
	@Test
	public void testPathToReg(){
		String r = Path.pathToReg("/api/:module/:id/name");
		Assert.assertEquals(r, "^/api/(?<module>[^/]+)/(?<id>[^/]+)/name$");
		Pattern p = Pattern.compile(r);
		Matcher m = p.matcher("/api/user/12/name");
		m.find();
		Assert.assertEquals(m.group("module"), "user");
		Assert.assertEquals(m.group("id"), "12");
	}
	@Test
	public void testPathToReg1(){
		String r = Path.pathToReg("/:module/");
		Assert.assertEquals(r, "^/(?<module>[^/]+)/$");
	}
	@Test
	public void testPathToReg2(){
		String r = Path.pathToReg("/:module");
		Assert.assertEquals(r, "^/(?<module>[^/]+)$");
	}
	@Test
	public void testPathToRegEmpty(){
		String r = Path.pathToReg("");
		Assert.assertEquals(r, "^$");
	}
	@Test
	public void testPathToRegNotVar(){
		String r = Path.pathToReg("/user/name");
		Assert.assertEquals(r, "^/user/name$");
	}

}
