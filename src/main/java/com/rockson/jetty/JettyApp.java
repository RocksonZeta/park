package com.rockson.jetty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.rockson.rest.App;
import com.rockson.rest.AppException;
import com.rockson.rest.Handle;
import com.rockson.rest.Middle;
import com.rockson.rest.Middleware;
import com.rockson.rest.Next;
import com.rockson.rest.PatternHandle;
import com.rockson.rest.utils.Path;

public class JettyApp extends AbstractHandler implements App {
	private static final Logger LOG = Log.getLogger(JettyApp.class);

	public final Server server;
	private final List<Middleware> middlewares = new ArrayList<>();

	// url -> method -> handles
	public Map<String, Map<String, Handle>> handles = new HashMap<>();
	public List<PatternHandle> patternHandles = new ArrayList<PatternHandle>();

	private void doMiddle(JettyRequest req, JettyResponse res, Next next) {
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

	private void doHandle(JettyRequest req, JettyResponse res) {
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

	@Override
	public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		LOG.info("handle - "+ target);
		JettyRequest req = new JettyRequest(target, baseRequest, request);
		JettyResponse res = new JettyResponse(response);
		Next next = new Next();
		next.onNext(() -> {
			this.doMiddle(req, res, next);
//			return true;
		});
		this.doMiddle(req, res, next);
		baseRequest.setHandled(true);
	}

	public JettyApp(int port) {
		server = new Server(port);
		server.setHandler(this);
	}

	@Override
	public void listen() {
		try {
			server.start();
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
	
	private Map<String, String> env = new HashMap<>();

	@Override
	public void set(String name, String value) {
		env.put(name, value);
	}

	@Override
	public void get(String name) {
		env.get(name);
	}

	@Override
	public void enable(String name) {
		
	}

	@Override
	public void enabled(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disable(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disabled(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void use(String path, Middle middle) {
		middlewares.add(new Middleware(null, path, middle));
	}

	@Override
	public void use(String method, String path, Middle middle) {
		middlewares.add(new Middleware(null == method ? null : method.toUpperCase(), path, middle));
	}

	@Override
	public void use(Middle middle) {
		middlewares.add(new Middleware(null, null, middle));
	}

	@Override
	public void engine(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void param(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void routing(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void route(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void locales(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void path(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mountPath(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMount(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(String path, Handle handle) {
		this.method("GET", path, handle);

	}

	@Override
	public void post(String path, Handle handle) {
		this.method("POST", path, handle);
	}

	@Override
	public void put(String path, Handle handle) {
		this.method("PUT", path, handle);
	}

	@Override
	public void del(String path, Handle handle) {
		this.method("DELETE", path, handle);
	}

	@Override
	public void method(String method, String path, Handle handle) {
		System.out.printf("method - method:%s , path:%s\n", method, path);
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
	public void get(Pattern regPath, Handle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(Pattern regPath, Handle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(Pattern regPath, Handle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void del(Pattern regPath, Handle handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void method(String method, Pattern regPath, Handle handle) {
		System.out.printf("method - method:%s , regPath:%s\n", method, regPath);
		this.patternHandles.add(new PatternHandle(method, regPath, handle));

	}

}
