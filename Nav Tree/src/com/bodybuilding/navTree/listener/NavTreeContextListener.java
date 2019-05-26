package com.bodybuilding.navTree.listener;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;

public class NavTreeContextListener 
               implements ServletContextListener{
	
	@Override
	public void contextDestroyed(ServletContextEvent ctx) {
		System.out.println("ServletContextListener destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent ctx) {
		try {
			InputStream is = ctx.getServletContext().getResourceAsStream("/navigation.json");
			if (is != null) {
				String inputJson = new String(IOUtils.toByteArray(is));
				ctx.getServletContext().setAttribute("json", inputJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("ServletContextListener started");	
	}
}
