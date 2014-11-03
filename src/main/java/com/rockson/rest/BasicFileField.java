package com.rockson.rest;

public class BasicFileField implements FileField {
	private String field;
	private String type;
	private String path;
	private long size;
	private String name;

	public BasicFileField(String field, String type, String path, long size, String name) {
		this.field = field;
		this.type = type;
		this.path = path;
		this.size = size;
		this.name = name;
	}

	@Override
	public String field() {
		return this.field;
	}

	@Override
	public String type() {
		return this.type;
	}

	@Override
	public String path() {
		return this.path;
	}

	@Override
	public long size() {
		return this.size;
	}

	@Override
	public String name() {
		return this.name;
	}
}
