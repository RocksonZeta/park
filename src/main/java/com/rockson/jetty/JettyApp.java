package com.rockson.jetty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.rockson.rest.AppException;
import com.rockson.rest.BasicApp;
import com.rockson.rest.Handle;
import com.rockson.rest.Middleware;
import com.rockson.rest.PatternHandle;

public class JettyApp extends AbstractHandler implements BasicApp {

	public Server server;
	private List<Middleware> middlewares = new ArrayList<>();

	// url -> method -> handles
	public Map<String, Map<String, Handle>> handles = new HashMap<>();
	public List<PatternHandle> patternHandles = new ArrayList<PatternHandle>();

	private Map<String, Object> env = new HashMap<>();

	@Override
	public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		this.handle(request,response);
		baseRequest.setHandled(true);
	}

	public JettyApp(int port) {
		server = new Server(port);
		server.setHandler(this);
	}
	public JettyApp() {
	}


	@Override
	public void listen() {
		try {
			server.start();
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
	
	
	@Override
	public Map<String, Map<String, Handle>> getHandles(){
		return this.handles;
	}
	@Override
	public List<PatternHandle> getPatternHandles(){
		return this.patternHandles;
	}
	@Override
	public Map<String, Object> getEnv(){
		return this.env;
	}
	@Override
	public List<Middleware> getMiddlewares() {
		return this.middlewares;
	}
	

	
}
