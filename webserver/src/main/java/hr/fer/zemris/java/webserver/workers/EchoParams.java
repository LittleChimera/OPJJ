package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.Date;

/**
 * Generates a HTML table and sends it as a response to the client.
 *
 * @see IWebWorker
 *
 * @author Luka Skugor
 *
 */
public class EchoParams implements IWebWorker {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * hr.fer.zemris.java.webserver.IWebWorker#processRequest(hr.fer.zemris.
	 * java.webserver.RequestContext)
	 */
	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		try {
			context.write("<table>");
			for (String paramName : context.getParameterNames()) {
				context.write("<tr><td>").write(paramName).write("</td>");
				context.write("<td>").write(context.getParameter(paramName))
				.write("</td></tr>");
			}
			context.write("</table>");
		} catch (IOException e) {
			System.err.println(e + new Date().toString());
		}
	}

}
