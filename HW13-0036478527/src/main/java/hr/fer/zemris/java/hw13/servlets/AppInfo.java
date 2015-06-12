package hr.fer.zemris.java.hw13.servlets;

import java.time.Duration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * AppInfo is a web listener which remembers system time when application
 * starts.
 * 
 * @author Luka Skugor
 *
 */
@WebListener
public class AppInfo implements ServletContextListener {

	/**
	 * Time when application started in milliseconds.
	 */
	private long startTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		startTime = System.currentTimeMillis();
		sce.getServletContext().setAttribute(ColorSetterServlet.BG_COLOR,
				ColorSetterServlet.DEFAULT_COLOR);
		sce.getServletContext().setAttribute("startTime", startTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println(formatElapsedTime(startTime));
	}

	/**
	 * Formats elapsed time as a string. Elapsed time is calculated as current
	 * time minus given start time.
	 * 
	 * Example of formatted time: 0 days 3 hours 2 minutes 1 seconds 321
	 * milliseconds
	 * 
	 * @param startTime
	 *            start time
	 * @return formatted elapsed time
	 */
	public static String formatElapsedTime(long startTime) {
		Duration duration = Duration.ofMillis(System.currentTimeMillis()
				- startTime);
		String elapsedTime = String
				.format("Server ran for %d days, %d hours, %d minutes, %d seconds, %d milliseconds.",
						duration.toDays(), duration.toHours(),
						duration.toMinutes(), duration.getSeconds() % 60,
						duration.getNano() / (int) 1e+6);

		return elapsedTime;
	}

}