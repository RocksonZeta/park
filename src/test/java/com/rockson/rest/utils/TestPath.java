package com.rockson.rest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import com.rockson.rest.Fn;
import com.rockson.rest.PathMatcher;

public class TestPath {
	@Test
	public void testPathToReg(){
		String r = Path.pathToReg("/api/:module/:id/name");
		Assert.assertEquals(r, "/api/(?<module>[^/]+?)/(?<id>[^/]+?)/name");
		Pattern p = Pattern.compile(r);
		Matcher m = p.matcher("/api/user/12/name");
//		System.out.println(m.toMatchResult());
		m.find();
		Assert.assertEquals(m.group("module"), "user");
		Assert.assertEquals(m.group("id"), "12");
	}
	@Test
	public void testPathToReg1(){
		String r = Path.pathToReg("/:module/");
		Assert.assertEquals(r, "/(?<module>[^/]+?)/");
	}
	@Test
	public void testPathToReg2(){
		String r = Path.pathToReg("/:module");
		Assert.assertEquals(r, "/(?<module>[^/]+?)");
	}
	@Test
	public void testPathToRegEmpty(){
		String r = Path.pathToReg("");
		Assert.assertEquals(r, "");
	}
	@Test
	public void testPathToRegNotVar(){
		String r = Path.pathToReg("/user/name");
		Assert.assertEquals(r, "/user/name");
	}
	
	@Test
	public void testMatch(){
		
//		Pattern pattern = Pattern.compile(Path.pathToReg("/:module/:id/name"));
		Pattern pattern = Pattern.compile("/(?<n>\\w+)/(\\w+?)/(\\w+?)/name");
		for(int i = 0 ;i < 10 ; i++){
			
			Map<String, String> result = Path.match(pattern,"/haha/user/123/name");
			System.out.println(result);
		}
	}
	
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("/(?<hello>[^/]+)");
		Map<String, String> result = Path.match(pattern,"/favicon.ico");
		System.out.println(result);
	}

}
