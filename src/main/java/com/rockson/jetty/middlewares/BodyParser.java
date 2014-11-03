package com.rockson.jetty.middlewares;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.rockson.rest.AppException;
import com.rockson.rest.BasicFileField;
import com.rockson.rest.FileField;
import com.rockson.rest.Middle;
import com.rockson.rest.utils.CollectionUtils;

public class BodyParser {

	private Map<String, Object> conf;
	private DiskFileItemFactory factory = new DiskFileItemFactory();
	private ServletFileUpload upload;

	/**
	 * @param conf
	 */
	public BodyParser() {
		this(Collections.emptyMap());
	}

	public BodyParser(Map<String, Object> conf) {
		this.conf = conf;
		factory.setSizeThreshold(null == conf.get("maxMemory") ? 8 * 1024 * 1024 : Integer.valueOf(conf
				.get("maxMemory").toString()));
		if (null != conf.get("tmpDir")) {
			factory.setRepository(new File(conf.get("tmpDir").toString()));
		}
		this.upload = new ServletFileUpload(factory);
		if (null != conf.get("maxFileSize")) {
			upload.setSizeMax(Integer.valueOf(conf.get("maxFileSize").toString()));
		}
	}

	public Middle apply() {
		return (req, res, next) -> {
			try {
				String queryString = req.req().getQueryString();
				String charset = (null == req.req().getCharacterEncoding() ? "UTF-8" : req.req().getCharacterEncoding());

				if (null != queryString && !"".equals(queryString)) {
					req.setQueries(processUrlEncodedString( queryString, charset));
				}
				if (!ServletFileUpload.isMultipartContent(req.req())) {
					String formString = IOUtils.toString(req.req().getInputStream(), charset);
					if (null == formString || !"".equals(formString)) {
						req.setFields(processUrlEncodedString(formString, charset));
					}
					next.apply();
					return;
				}
				Map<String, List<String>> fields = new HashMap<>();
				List<FileItem> items = this.upload.parseRequest(req.req());
				Map<String, List<FileField>> files = new HashMap<>();
				FileField fileField = null;
				for (FileItem item : items) {
					if (item.isFormField()) {
						CollectionUtils.add(fields, item.getFieldName(), item.getString());
					} else {
						fileField = processUploadedFile(item);
						CollectionUtils.add(files, fileField.field(), fileField);
					}
				}
				req.setFields(fields);
				req.setFiles(files);
			} catch (FileUploadException e) {
				throw new AppException(e);
			} catch (IOException e) {
				throw new AppException(e);
			}
			next.apply();
			return;
		};
	}

	// k1=v1&k2=v2
	protected Map<String, List<String>> processUrlEncodedString(String queryString, String charset) {
		String[] kvStrings = queryString.split("&");
		String key = null, value = null;
		String[] kv = null;
		Map<String, List<String>> map = new HashMap<>();
		for (String kvString : kvStrings) {
			kv = kvString.split("=");
			try {
				key = URLDecoder.decode(kv[0], charset);
				if (1 >= kv.length) {
					CollectionUtils.add(map, key, "");
					continue;
				}
				value = URLDecoder.decode(kv[1], charset);
				CollectionUtils.add(map, key, value);
			} catch (UnsupportedEncodingException e) {
				throw new AppException(e);
			}
		}
		return map;
	}

	protected BasicFileField processUploadedFile(FileItem item) {
		String fieldName = item.getFieldName();
		String fileName = item.getName();
		File dir = new File(null == this.conf.get("tmpDir") ? this.conf.get("tmpDir").toString()
				: System.getProperty("java.io.tmpdir"));
		if (dir.exists()) {
			dir.mkdirs();
		}
		Random r = new Random();
		String name = String.format("%x%x", r.nextLong(), r.nextLong())
				+ (-1 == fileName.lastIndexOf('.') ? "" : fieldName.substring(fileName.lastIndexOf('.')));
		File file = new File(dir, name);
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
			IOUtils.copy(item.getInputStream(), outputStream);
		} catch (IOException e) {
			throw new AppException(e);
		}
		return new BasicFileField(fieldName, item.getContentType(), file.getAbsolutePath(), item.getSize(), fileName);
	}

}
