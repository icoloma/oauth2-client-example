package com.acme.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceConfigListener extends GuiceServletContextListener {
	
	@Override
    protected Injector getInjector() {
		Injector instance = Guice.createInjector(
			new MyServletModule()
		);
		return instance;
    }

}
