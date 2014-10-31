package com.rockson.rest;



public class Next {
	private NextFn cb;
	public int count = 0;
	
	public void apply(){
		cb.apply();
	}
	
	public void onNext(NextFn fn){
		this.cb = fn;
	}

	@FunctionalInterface
	public interface NextFn{
		void apply();
	}
}
