package com.rockson.jetty;

import javax.servlet.http.HttpServletRequest;

import com.rockson.rest.AbstractRequest;
import com.rockson.rest.ViewRender;

public class JettyRequest extends AbstractRequest {
	public final org.eclipse.jetty.server.Request baseRequest;
	
	public final String originPath;
	
	public JettyRequest(String path , org.eclipse.jetty.server.Request baseRequest,HttpServletRequest request ) {
		this.originPath = path;
		this.baseRequest = baseRequest;
		this.req = request;
	}



	


}
