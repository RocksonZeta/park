package com.rockson.rest.jetty;

import org.apache.commons.io.IOUtils;

import com.rockson.jetty.JettyApp;
import com.rockson.jetty.middlewares.BodyParser;
import com.rockson.jetty.middlewares.Static;
import com.rockson.rest.App;

public class TestJettyApp {

	public static void main(String[] args) {
		App app = new JettyApp(8000);
		app.use((req,res,next)->{
			next.apply(null);
			return true;
		});
		app.use(new Static("/").apply());
		app.use(new BodyParser().apply());
		app.get("/", (req,res)->{
			res.send("hello");
		});
		app.get("/:name", (req,res)->{
			res.send(req.param("name"));
		});
		app.post("/test/body", (req,res)->{
//			System.out.println(req.req().get);
			System.out.println("query string:"+req.req().getQueryString());
			try {
				System.out.println("body");
				IOUtils.copy(req.req().getInputStream(), System.out);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(req.req().getParameterMap());
			System.out.println("test body");
			res.json(req.body());
		});
		app.listen();
	}
}
