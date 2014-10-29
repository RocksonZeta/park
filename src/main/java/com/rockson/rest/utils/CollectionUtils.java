package com.rockson.rest.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
	
	public static <K,V> void add(Map<K, List<V>> map , K key , V value){
		if(map.containsKey(key)){
			map.get(key).add(value);
		}else{
			List<V> list = new ArrayList<>();
			list.add(value);
			map.put(key, list);
		}
	}

}
