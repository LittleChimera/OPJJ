package hr.fer.zemris.java.hw13.servlets;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionInitializer implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute(ColorSetterServlet.BG_COLOR, ColorSetterServlet.DEFAULT_COLOR);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
	}
}
