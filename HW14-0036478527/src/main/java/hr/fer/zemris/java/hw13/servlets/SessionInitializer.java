package hr.fer.zemris.java.hw13.servlets;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Initializer of session parameters.
 * 
 * @author Luka Skugor
 *
 */
@WebListener
public class SessionInitializer implements HttpSessionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
	 * .HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute(ColorSetterServlet.BG_COLOR,
				ColorSetterServlet.DEFAULT_COLOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
	 * .http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
	}
}
