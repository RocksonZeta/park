package com.rockson.rest.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {
	
	public static boolean isNamedPath(String path){
		return -1 != path.indexOf(':');
	}
	
	
	/**
	 *	/:module/:id/name ->    /(?<module>.+?)/(?<id>.+?)/name
	 * 
	 * @param path
	 */
	public static final Pattern PATH_REG = Pattern.compile(":([^/]+)");
	public static String pathToReg(String path){
		Matcher matcher = PATH_REG.matcher(path);
		while(matcher.find()){
			path = matcher.replaceAll("(?<$1>[^/]+)");
		}
		return path;
	}
	
	public static final Pattern GROUP_REG = Pattern.compile("\\((\\?\\<(.+?)\\>)?.*?\\)");
	
	public static Map<String, String> match(Pattern pattern ,String path){
		System.out.println("match "+pattern+" "+path);
		Matcher matcher = pattern.matcher(path);
		if(!matcher.find()){
			return null;
		}
		Map<String , String> result = new LinkedHashMap<>();
		Matcher groupMatcher = GROUP_REG.matcher( pattern.pattern());
		List<Object> names = new LinkedList<>();
		while(groupMatcher.find()){
			if(groupMatcher.groupCount()>0){
				String groupName = groupMatcher.group(2);
				if(null == groupName){
					names.add(names.size());
				}else{
					names.add(groupMatcher.group(2));
				}
			}
		}
		for (Object name : names) {
			if(name instanceof Integer){
				result.put(String.valueOf(name), matcher.group((Integer)name+1));
			}else{
				result.put(name.toString(), matcher.group(name.toString()));
			}
		}
		
		return result;
	}
	

}
