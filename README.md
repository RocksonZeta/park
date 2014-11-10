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

## API

interface App
====
An application interface. To create http server,we can implement this interface or implement `BasicApp` interface for short.
###void set(String name , String value)
Set a environment variable for the app.
###void get(String name)
Get a environment variable from the app.
###void enable(String name )
Set a environment variable to be `true` for the app.
###boolean enabled(String name )
Get a environment variable from the app as a boolean format.
###void disable(String name )
Set a environment variable to be `false` for the app.
###boolean disabled(String name )
Get a environment variable from the app as a boolean format.
###void use(Middle middle)
Add a middleware for the app. `Middle` is `@FunctionalInterface` ,signatrue is `(Request req, Response res ,Next next)->{};` 
###void use(String path ,Middle middle)
Add a middleware with a specified path for the app. 
###void use(Pattern pathPattern ,Middle middle)
Add a middleware with a specified pattern path for the app.
###void use(String method,String path ,Middle middle)
Add a middleware with a specified path and request method for the app. 
###void use(String method,Pattern pathPattern ,Middle middle);
Add a middleware with a specified pattern path and request method for the app. 
###Map\<String, Map\<String, Handle\>\> routes()
Get all routes from the app.
###void listen()
Start to listen an the specified port.
###void handle(HttpServletRequest request,HttpServletResponse response)
If we want create own http server, we must implement this function to handle all requests. 

interface Reqest
================
###String param(String name)
Get param from url params. eg. in `/user/:id. req.param("id")`.
###String query(String name)
Get param from url query string. eg. in `/usrs?age=12. req.query('age')`.
###String body(String name)
Get param from request body. eg. in `post body age=12. req.query('age')`.
###FileField file(String name)
Get upload file object.
###String getParam(String name ,String... defaultValue)
Find param in the request. find place: url params -> query -> body -> defaultValue.
###Map\<String,String\> body()
Get request body as map;
###List\<String\> queries(String name)
Get multi-params from the url query string.
###List\<String\> bodies(String name)
Get multi-params from the request body.
###List\<FileField\> files(String name)
Get multi-params from request files.
###List\<String\> getParams(String name)
Find multi-params from request.find place: url params -> query -> body -> defaultValue.
###Cookie cookie(String name)
Get a cookie
###Cookie[] cookies()
Get all cookies
###String method();
Get http request method.
###String get(String field);
Get http head value.
###String ip()
Return the remote address, or when "trust proxy" is enabled - the upstream address.
###List<String> ips()
When "trust proxy" is `true`, parse the "X-Forwarded-For" ip address list and return an array, otherwise an empty array is returned. For example if the value were "client, proxy1, proxy2" you would receive the array ["client", "proxy1", "proxy2"] where "proxy2" is the furthest down-stream.
###String path()
Returns the request URL pathname.
###String hostname()
Returns the hostname from the "Host" header field (void of portno).
###boolean xhr()
###String protocol()
Return the protocol string "http" or "https" when requested with TLS. When the "trust proxy" setting is enabled the "X-Forwarded-Proto" header field will be trusted. If you're running behind a reverse proxy that supplies https for you this may be enabled.
###boolean secure()
Check if a TLS connection is established. This is a short-hand for: 'https'.equals(req.protocol())
###String originalUrl()
it retains the original request url
###HttpServletRequest req()
###Map\<String,List\<FileField\>\> files()
Get all file objects from upload files.
###Map\<String, List\<String>> fields()
Get all params from the body.
###Map\<String, List\<String\>\> queries()
Get all params from url query.
###Map\<String, String\> params()
Get all params from url params.
###Session session()
request Session
###void session(Session session)
###Map\<Object , Object\> var()
Get all varibles.
###Object var(Object key)
Get varible from request's variables.
###void var(Object key,Object value)
Set varible from request's variables.
###App app()
###void app(App app)

For extends only
----------------
###void setParams(Map\<String, String\> params)
###void setQueries(Map\<String, List\<String\>\> queries)
###void setFields(Map\<String, List\<String\>\> fields)
###void setFiles(Map\<String, List\<FileField\>\> files)


interface Response
==================
###void status(int code)
Set http response status.
###void set(String field , String value)
Set http response header.
###String get(String field)
Get http response header.
###void cookie(String name ,String value)
Set response cookie.
###void cookie(String name ,String value, int maxAge)
Set response cookie with maxAge(unit is second).
###void cookie(Cookie cookie)
Set a cookie.
###void clearCookie(String name)
clear a specfied cookie.
###void redirect(String url)
Redirect to the given url.
###void redirect(int status, String url)
Redirect to the given url with optional status code.
###void location(String location)
Set location header.
###void send(String body)
Send a response.
###void send(InputStream inputStream)
Send a response. Close inputStream automaticly.
###void send(Reader reader)
Send a response.Close reader automaticly.
###void json(Object body)
Send object a json.
###void jsonp(Object body)
Send object a json in jsonp. the method is `callback({...})`;
###void jsonp(Object body,String method)
jsonp with specified method.
###void type(String type)
set content type.
###HttpServletResponse res()
###void bufferSize(int size)
set response bufferSize.
###void render(String tpl , Map\<String, ?\> data)
Render a view with data.

used by middleware
-----------------
###App app()
###void app(App app)
###void setViewRender(ViewRender render)


Middleware
=========
Frequently-used middlewares
###BodyParser
To parse post body and upload files.
constructor:
-----------
####BodyParser(Map\<String, Object\> conf)

###LocalSession
Provide session locally.
constructor: 
-----------
####LocalSession(Map\<String,Object\> conf)

###RedisSession
Using Redis store as session store.
constructor:
-----------
####RedisSession(String host, int port , Map\<String, Object\> conf)

###ParkFreeMarker
Use freemarker as a render.
constructor:
-----------
####ParkFreeMarker(Map\<String, Object\> conf)

###Static
Enable app has a static resource server ability.
constructor:
-----------
Static(String dir)


how to write a middleware
-----------------------
To write middleware is simple.app's middlewares like a stack execute orderly. Basic example:
```java
public class MyMiddleware {
	public Middle apply() {
		return (req,res,next)->{
			//to do before handle
			next.apply();	//should invoke next.apply() or res.send|json...
			//to do after handled.
		}
	}
}
//and then we can use it
app.use(new MyMiddleware().apply());

```


How to extends
============


How to write our own http server:
=================================
