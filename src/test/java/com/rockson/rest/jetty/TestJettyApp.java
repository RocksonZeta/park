package com.rockson.rest.jetty;

import com.rockson.jetty.JettyApp;
import com.rockson.rest.App;

public class TestJettyApp {

	
	public static void main(String[] args) {
		App app = new JettyApp(8000);
		app.use((req,res,next)->{
			System.out.println("m1");
			next.apply(null);
			System.out.println("m1 end");
			return true;
		});
		app.use((req,res,next)->{
			System.out.println("m2");
			next.apply(null);
			System.out.println("m2 end");
			return true;
		});
		app.get("/", (req,res)->{
			System.out.println("handle");
			res.send("hello");
		});
		app.get("/:name/:id", (req,res)->{
			System.out.println("param hello:"+req.params("name"));
		});
		app.listen();
	}
}
