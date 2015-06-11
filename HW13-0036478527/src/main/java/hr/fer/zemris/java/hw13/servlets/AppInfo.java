package hr.fer.zemris.java.hw13.servlets;

import java.time.Duration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInfo implements ServletContextListener {

	private long startTime;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		startTime = System.currentTimeMillis();
		sce.getServletContext().setAttribute(ColorSetterServlet.BG_COLOR,
				ColorSetterServlet.DEFAULT_COLOR);
		sce.getServletContext().setAttribute("startTime", startTime);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println(formatElapsedTime(startTime));
	}

	public static String formatElapsedTime(long startTime) {
		Duration duration = Duration.ofMillis(System.currentTimeMillis()
				- startTime);
		String elapsedTime = String
				.format("Server ran for %d days, %d hours, %d minutes, %d seconds, %d milliseconds.",
						duration.toDays(), duration.toHours(),
						duration.toMinutes(), duration.getSeconds(),
						duration.getNano() / (int)1e+6);

		return elapsedTime;
	}

}