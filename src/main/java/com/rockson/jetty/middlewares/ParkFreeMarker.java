package com.rockson.jetty.middlewares;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import com.rockson.rest.AppException;
import com.rockson.rest.Middle;
import com.rockson.rest.ViewRender;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

public class ParkFreeMarker implements ViewRender {
	Configuration configuration;

	public ParkFreeMarker() {
		this(Collections.emptyMap());
	}

	@SuppressWarnings("unchecked")
	public ParkFreeMarker(Map<String, Object> conf) {
		configuration = new Configuration(conf.containsKey("version") ? (Version) conf.get("version")
				: Configuration.VERSION_2_3_21);
		if (conf.containsKey("numberFormat")) {
			configuration.setNumberFormat(conf.get("numberFormat").toString());
		} else {
			configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(conf.containsKey("version") ? (Version) conf
					.get("version") : Configuration.VERSION_2_3_21).build());
		}
		if (conf.containsKey("numberFormat")) {
			configuration.setNumberFormat(conf.get("numberFormat").toString());
		}
		if (conf.containsKey("dateFormat")) {
			configuration.setDateFormat(conf.get("dateFormat").toString());
		}
		if (conf.containsKey("timeFormat")) {
			configuration.setTimeFormat(conf.get("timeFormat").toString());
		}
		if (conf.containsKey("dateTimeFormat")) {
			configuration.setDateTimeFormat(conf.get("dateTimeFormat").toString());
		}
		if(conf.containsKey("cache")){
			configuration.setCacheStorage((freemarker.cache.CacheStorage) conf.get("cache"));
		}else{
			configuration.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250));
		}
		
		if (conf.containsKey("classForTemplateLoading")) {
			configuration.setClassForTemplateLoading((Class) conf.get("classForTemplateLoading"), "");
		}
		if (conf.containsKey("dir")) {
			try {
				configuration.setDirectoryForTemplateLoading(new File(conf.get("dir").toString()));
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
		if (conf.containsKey("templateLoader")) {
			configuration.setTemplateLoader((TemplateLoader) conf.get("templateLoader"));
		}

		try {
			if (conf.containsKey("sharedVariables")) {
				configuration.setSharedVaribles((Map<String, Object>) conf.get("dateTimeFormat"));
			}
			for (Map.Entry<String, Object> entry : conf.entrySet()) {
				if (entry.getValue() instanceof TemplateModel) {
					configuration.setSharedVariable(entry.getKey(), entry.getValue());
				}
			}
		} catch (TemplateModelException e) {
			throw new AppException(e);
		}
	}

	public Middle apply() {

		return (req, res, next) -> {
			res.setViewRender(this);
			next.apply();
		};

	}

	@Override
	public String render(String tpl, Map<String, ?> data) {
		try {
			Template template = configuration.getTemplate(tpl);
			StringWriter stringWriter = new StringWriter(4 * 1024);
			template.process(data, stringWriter);
			return stringWriter.getBuffer().toString();
		} catch (IOException | TemplateException e) {
			throw new AppException(e);
		}
	}

}
