package com.rockson.jetty.middlewares;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
					res.send(new FileInputStream(file));
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
