package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Date;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		try {
			context.write("<table>");
			for (String paramName : context.getParameterNames()) {
				context.write("<tr><td>").write(paramName).write("</td>");
				context.write("<td>").write(context.getParameter(paramName)).write("</td></tr>");
			}
			context.write("</table>");			
		} catch (IOException e) {
			System.err.println(e + new Date().toString());
		}
	}

}
