package com.acme.config;

import com.acme.resources.Events;
import com.acme.resources.Root;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Set;

/**
 * Configure web stuff
 * @author icoloma
 *
 */
@SuppressWarnings("rawtypes")
public class MyServletModule extends ServletModule {

	@Override
	@SuppressWarnings("unchecked")
    protected void configureServlets() {
		
		// bind resources
		for (Class resourceClass : getResources()) {
        	bind(resourceClass);
		}

        bind(ObjectMapper.class).toInstance(new ObjectMapper());
        bind(DatastoreService.class).toInstance(DatastoreServiceFactory.getDatastoreService());

		// Exception mapping
		//bind(MyExceptionMapper.class);

        bind(OAuthConfig.class).toInstance(new OAuthConfigImpl());

		// jersey customization
        filter("/*").through(
            GuiceContainer.class,
        	ImmutableMap.<String, String>builder()
        	
        		// all files relative to /WEB-INF/jsp 
	    		.put("com.sun.jersey.config.property.JSPTemplatesBasePath", "/WEB-INF/jsp")
	    		
	    		// workaround for http://java.net/jira/browse/JERSEY-630
	    		.put("com.sun.jersey.config.feature.DisableWADL", "true") 
	    		
	    		// do not intercept /_ah/* requests
	    		.put(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX, "/_ah/.*")
	    		
	    		// use Jackson to serialize
	    		.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true") 
	    		
	    		.build()		
		);
        
    }

    // add here your resource classes
    private Set<Class> getResources() {
        return Sets.newHashSet(new Class[] {
			Root.class,
            Events.class
		});
	}

}
