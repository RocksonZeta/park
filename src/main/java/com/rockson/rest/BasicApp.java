package com.rockson.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rockson.rest.utils.Path;

public interface BasicApp extends App {
	
	List<Middleware> getMiddlewares();
	// url -> method -> handles
	Map<String, Map<String, Handle>> getHandles();
	List<PatternHandle> getPatternHandles();
	Map<String, String> getEnv();

	default void doMiddle(ServletRequest req, ServletResponse res, Next next) {
		List<Middleware> middlewares = getMiddlewares();
		if (next.count >= middlewares.size()) {
			doHandle(req, res);
			return;
		}
		Middleware middleware = middlewares.get(next.count);
		next.count++;
		if (middleware.match(req.method(), req.path())) {
			middleware.middle.apply(req, res, next);
		} else {
			doMiddle(req, res, next);
		}
	}

	default void doHandle(ServletRequest req, ServletResponse res) {
		Map<String, Map<String, Handle>> handles = getHandles();
		List<PatternHandle> patternHandles = getPatternHandles();
		Handle handle = null;
		Map<String, Handle> methodHandles = handles.get(req.path());
		if (null != methodHandles) {
			handle = methodHandles.get(req.method());
		}
		if (null != handle) {
			System.out.println("match method"+req.method());
			handle.apply(req, res);
			return;
		}
		for (PatternHandle patternHandle : patternHandles) {
			Map<String, String> params = patternHandle.match(req.method(), req.path());
			if (null != params) {
				System.out.println("match pattern "+ patternHandle);
				req.setParams(params);
				patternHandle.handle.apply(req, res);
				break;
			}
		}
	}

	default void exec(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ServletRequest req = new ServletRequest(request);
		ServletResponse res = new ServletResponse(response);
		Next next = new Next();
		next.onNext(() -> {
			this.doMiddle(req, res, next);
		});
		this.doMiddle(req, res, next);
	}


	@Override
	default void listen() {
	}
	
	@Override
	default void set(String name, String value) {
		getEnv().put(name, value);
	}

	@Override
	default void get(String name) {
		getEnv().get(name);
	}

	@Override
	default void enable(String name) {
		
	}

	@Override
	default void enabled(String name) {
		
	}

	@Override
	default void disable(String name) {

	}

	@Override
	default void disabled(String name) {

	}

	@Override
	default void use(String path, Middle middle) {
		getMiddlewares().add(new Middleware(null, path, middle));
	}

	@Override
	default void use(String method, String path, Middle middle) {
		getMiddlewares().add(new Middleware(null == method ? null : method.toUpperCase(), path, middle));
	}

	@Override
	default void use(Middle middle) {
		getMiddlewares().add(new Middleware(null, null, middle));
	}

	@Override
	default void engine(String path) {

	}

	@Override
	default void param(String path) {

	}

	@Override
	default void routing(String path) {

	}

	@Override
	default void route(String path) {

	}

	@Override
	default void locales(String path) {

	}

	@Override
	default void render(String path) {

	}

	@Override
	default void path(String path) {

	}

	@Override
	default void mountPath(String path) {

	}

	@Override
	default void onMount(String path) {

	}

	@Override
	default void get(String path, Handle handle) {
		this.method("GET", path, handle);

	}

	@Override
	default void post(String path, Handle handle) {
		this.method("POST", path, handle);
	}

	@Override
	default void put(String path, Handle handle) {
		this.method("PUT", path, handle);
	}

	@Override
	default void del(String path, Handle handle) {
		this.method("DELETE", path, handle);
	}

	@Override
	default void method(String method, String path, Handle handle) {
		Map<String, Map<String, Handle>> handles = getHandles();
		if (Path.isNamedPath(path)) {
			method(method, Pattern.compile(Path.pathToReg(path)), handle);
			return;
		}
		if (handles.containsKey(path)) {
			Map<String, Handle> methodHandles = handles.get(path);
			methodHandles.put(method.toUpperCase(), handle);
		} else {
			Map<String, Handle> methodHandles = new HashMap<>();
			methodHandles.put(method.toUpperCase(), handle);
			handles.put(path, methodHandles);
		}

	}

	@Override
	default void get(Pattern regPath, Handle handle) {
		this.method("GET", regPath, handle);
	}

	@Override
	default void post(Pattern regPath, Handle handle) {
		this.method("POST", regPath, handle);
	}

	@Override
	default void put(Pattern regPath, Handle handle) {
		this.method("PUT", regPath, handle);
	}

	@Override
	default void del(Pattern regPath, Handle handle) {
		this.method("DELETE", regPath, handle);
	}

	@Override
	default void method(String method, Pattern regPath, Handle handle) {
		getPatternHandles().add(new PatternHandle(method, regPath, handle));

	}

}
