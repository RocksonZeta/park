park
====

Easily to construct restful app like express!


##Example:
```java
public static void main(String[] args) {
	App app = new JettyApp(8000);
	app.use((req,res,next)->{
		try{
			next.apply();
		} catch(Exception e){
			System.out.println(e.getClass());
			e.printStackTrace();
		}
	});
	app.use((req,res,next)->{
		long begin = System.currentTimeMillis(); 
		next.apply();
		long end = System.currentTimeMillis();
		System.out.printf("%s %s +%dms\n" , req.method(),req.url(),(end- begin));
	});
	app.use(Pattern.compile("^/admin/.*$"),(req,res,next)->{
		System.out.println(req.get("cookie"));
		next.apply();
	});
	app.use(new Static("/").apply());
	app.use(new BodyParser().apply());
	app.use(new LocalSession().apply());
	app.use(new ParkFreeMarker(new HashMap<String, Object>(){{put("dir","/");}}).apply());
	app.get("/", (req,res)->{
		res.send("hello");
	});
	app.get("/user/:id", (req,res)->{
		res.send(req.param("id"));
	});
	app.post("/test/body", (req,res)->{
		res.json(req.body());
	});
	app.listen();
}
```