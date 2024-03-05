package com.app.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionListener implements HttpSessionListener {
	
	private final Logger log = LoggerFactory.getLogger(HttpSessionListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.info("inside sessionCreated");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.info("inside sessionDestroyed.");
	}

}
