package com.rockson.rest;

import com.rockson.rest.utils.Fn1;


//@FunctionalInterface
public class Next {
	private Throwable e;
	private Fn1<Throwable,Void> cb;
	public int count = 0;
	
//	public Next(Fn1<Throwable,Void> fn) {
//		cb = fn;
//	}
	public void apply(Throwable e){
		cb.apply(e);
	}
	
	public void onNext(Fn1<Throwable,Void> fn){
		this.cb = fn;
	}


}
