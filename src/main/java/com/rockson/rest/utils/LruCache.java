package com.rockson.rest.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class LruCache<K, V> extends LinkedHashMap<K, V> {
	private final int maxEntries;
	public LruCache(final int maxEntries) {
		super(maxEntries + 1, 1.0f, true);
		this.maxEntries = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return super.size() > maxEntries;
	}
	
	public static <K, V> Map<K, V> create(int max, Class<K> k , Class<V> v){
		return Collections.synchronizedMap(new LruCache<K, V>(max) );
	}
}