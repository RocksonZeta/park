package com.rockson.jetty.middlewares;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.rockson.rest.AppException;
import com.rockson.rest.Middle;

public class Static {
	private File dir;
	public Static(String dir) {
		this.dir = new File(dir);
	}

	public Middle apply() {
		
		return  (req ,res,next)->{
			String path = req.originalUrl();
			File file = new File(dir , path);
			if(file.exists()&& file.isFile()){
				try {
					DateFormat dateFormat =new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);  
					dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
					if(null!=req.get("If-Modified-Since")){
						try {
							if(dateFormat.parse(req.get("If-Modified-Since")).getTime()/1000 >= file.lastModified()/1000){
								res.status(304);
								return;
							}
						} catch (ParseException e) {
							e.printStackTrace();
							//go on
						}
					}else{
						res.set("Last-Modified",dateFormat.format(file.lastModified()));
						file.lastModified();
						res.send(new FileInputStream(file));
					}
				} catch (FileNotFoundException e) {
					throw new AppException(e);
				}
				return ;
			}
			next.apply();
			return ;
		};
	}
}
