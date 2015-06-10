package hr.fer.zemris.java.tecaj_12.servleti;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Informacije implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Dojava da je aplikacija pokrenuta!");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Dojava da se aplikacija gasi!");
	}

}