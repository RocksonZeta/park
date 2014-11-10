package com.rockson.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rockson.rest.utils.Path;

public interface BasicApp extends App {
	
	List<Middleware> getMiddlewares();
	// url -> method -> handles
	Map<String, Map<String, Handle>> getHandles();
	List<PatternHandle> getPatternHandles();
	Map<String, Object> getEnv();

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
			handle.apply(req, res);
			return;
		}
		for (PatternHandle patternHandle : patternHandles) {
			Map<String, String> params = patternHandle.match(req.method(), req.path());
			if (null != params) {
				req.setParams(params);
				patternHandle.handle.apply(req, res);
				break;
			}
		}
	}

	default void handle(HttpServletRequest request,	HttpServletResponse response) {
		ServletRequest req = new ServletRequest(this,request);
		ServletResponse res = new ServletResponse(this,response);
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
		getEnv().put(name ,true);
	}

	@Override
	default boolean enabled(String name) {
		return (Boolean)getEnv().get(name);
	}

	@Override
	default void disable(String name) {
		getEnv().put(name, false);
	}

	@Override
	default boolean disabled(String name) {
		return !(Boolean)getEnv().get(name);
	}
	
	@Override
	default void use(String path, Middle middle) {
		getMiddlewares().add(new Middleware(null, Path.pathToPattern(path), middle));
	}
	@Override
	default void use(Pattern pathPattern, Middle middle) {
		getMiddlewares().add(new Middleware(null, pathPattern, middle));
	}

	@Override
	default void use(String method, String path, Middle middle) {
		getMiddlewares().add(new Middleware(null == method ? null : method.toUpperCase(), Path.pathToPattern(path), middle));
	}
	@Override
	default void use(String method, Pattern pathPattern, Middle middle) {
		getMiddlewares().add(new Middleware(null == method ? null : method.toUpperCase(),pathPattern, middle));
	}

	@Override
	default void use(Middle middle) {
		getMiddlewares().add(new Middleware(null, null, middle));
	}

	@Override
	default Map<String, Map<String, Handle>> routes() {
		return getHandles();
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
