package hr.fer.zemris.java.webserver;

/**
 * This class represents a worker which can do a job on {@link SmartHttpServer}.
 * The result of the job is written to response to a client.
 *
 * For example, we could create a job which echoes "Hello world!". It should
 * write that string to the given context which is automatically sent to the
 * client.
 *
 * @author Luka Skugor
 *
 */
public interface IWebWorker {

	/**
	 * Processes a client request by doing its job and writing a response to the
	 * client.
	 *
	 * @param context
	 *            context of the response to the client
	 */
	public void processRequest(RequestContext context);

}
