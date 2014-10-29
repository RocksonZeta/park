package com.rockson.jetty.middlewares;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.rockson.jetty.JettyFileField;
import com.rockson.rest.AppException;
import com.rockson.rest.FileField;
import com.rockson.rest.Middle;
import com.rockson.rest.Request;
import com.rockson.rest.utils.CollectionUtils;

public class BodyParser {
	
	private Map<String, Object> conf;
	private DiskFileItemFactory factory = new DiskFileItemFactory();
	private ServletFileUpload upload;
	
	/**
	 * 
	 * 
	 * @param conf
	 */
	public BodyParser() {
		this(Collections.emptyMap());
	}
	public BodyParser(Map<String, Object> conf) {
		this.conf = conf;
		factory.setSizeThreshold(null == conf.get("maxMemory")?8*1024*1024:Integer.valueOf(conf.get("maxMemory").toString()));
		if(null!= conf.get("tmpDir")){
			factory.setRepository(new File(conf.get("tmpDir").toString()));
		}
		this.upload = new ServletFileUpload(factory);
		if(null !=conf.get("maxFileSize")){
			upload.setSizeMax(Integer.valueOf(conf.get("maxFileSize").toString()));
		}
	}
	
	public Middle apply(){
		return  (req ,res,next)->{
			try {
				if(!ServletFileUpload.isMultipartContent(req.req())){
					next.apply(null);
					return true;
				}
				List<FileItem> items = this.upload.parseRequest(req.req());
				for(FileItem item :items){
					if (item.isFormField()) {
					    CollectionUtils.add(req.fields(), item.getFieldName(), item.getString());
				    } else {
				        processUploadedFile(req,item);
				    }
				}
			} catch (FileUploadException e) {
				next.apply(new RuntimeException(e));
				return false;
			}
			next.apply(null);
			return true;
		};
	}
	
	//k1=v1&k2=v2
	protected void processUrlEncodedString(String queryString){
		String[] kvStrings = queryString.split("&");
		String key = null,value = null;
		String[] kv = null;
		for(String kvString :kvStrings){
			kv = kvString.split("=");
			try {
				key = URLDecoder.decode(kv[0],"UTF-8" );
			} catch (UnsupportedEncodingException e) {
				throw new AppException(e);
			}
		}
	}

	protected void processUploadedFile(Request req, FileItem item) {
		String fieldName = item.getFieldName();
	    String fileName = item.getName();
	    File dir = new File(null == this.conf.get("tmpDir")?this.conf.get("tmpDir").toString():System.getProperty("java.io.tmpdir"));
	    if(dir.exists()){
	    	dir.mkdirs();
	    }
	    Random r = new Random();
	    String name =  String.format("%x%x", r.nextLong(), r.nextLong())+(-1==fileName.lastIndexOf('.')?"":fieldName.substring(fileName.lastIndexOf('.')));
	    File file = new File(dir,name);
	    try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
			IOUtils.copy(item.getInputStream(), outputStream);
		} catch (IOException e) {
			throw new AppException(e);
		}
	    CollectionUtils.add(req.files(), fieldName, new JettyFileField(fieldName, item.getContentType(), file.getAbsolutePath(), item.getSize(), fileName));
	}

}
