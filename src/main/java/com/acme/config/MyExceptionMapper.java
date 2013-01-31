package com.acme.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @author icoloma
 */
@Provider
@Singleton
public class MyExceptionMapper implements ExceptionMapper<Throwable> {

	private static Logger log = LoggerFactory.getLogger(MyExceptionMapper.class);
	
	@Override
	public Response toResponse(Throwable root) {
		Throwable exception = root;
		if (exception instanceof WebApplicationException) {
			return ((WebApplicationException)exception).getResponse();
		}
		log.error(exception.getMessage(), exception);
		while (exception != null) {
			if (exception instanceof RuntimeException) {
				exception = exception.getCause();
			} else {
				break;
			}
		}
		return Response.status(500).build();
	}

}
