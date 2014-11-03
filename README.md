park
====

Easily to construct restful app like express!


##Example:
```java
public static void main(String[] args) {
	App app = new JettyApp(8000);
	app.use((req,res,next)->{
		System.out.println(req.path()+" --->m1");
		next.apply();
		System.out.println(req.path()+" m1--->");
	});
	app.use("/haha",(req,res,next)->{
		res.json("haha");
//			next.apply();
	});
	app.use(new Static("/").apply());
	app.use(new BodyParser().apply());
	app.use(new LocalSession().apply());
	app.use(new ParkFreeMarker(new HashMap<String, Object>(){{put("dir","/");}}).apply());
	app.get("/", (req,res)->{
		res.send("hello");
	});
	app.get("/:name", (req,res)->{
		
		if(req.session().contains("count")){
			req.session().set("count", (Integer)(req.session().get("count"))+1);
		}else{
			req.session().set("count", 1);
		}
		res.cookie("name", "jim");
//			res.send(req.param("name")+req.session().get("count"));
		res.render("/hello1.txt", req.params());
	});
	app.post("/test/body", (req,res)->{
		res.json(req.body());
	});
	app.listen();
}
```