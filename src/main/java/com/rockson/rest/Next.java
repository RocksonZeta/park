package com.rockson.rest;

import com.rockson.rest.utils.Fn1;


public class Next {
	private Fn1<RuntimeException,Void> cb;
	public int count = 0;
	
	public void apply(RuntimeException e){
		cb.apply(e);
	}
	
	public void onNext(Fn1<RuntimeException,Void> fn){
		this.cb = fn;
	}


}
