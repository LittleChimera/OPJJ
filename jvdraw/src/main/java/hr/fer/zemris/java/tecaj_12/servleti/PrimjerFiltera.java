package hr.fer.zemris.java.tecaj_12.servleti;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/dr")
public class PrimjerFiltera implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("Ušao sam u filter!");
		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
		System.out.println("Izašao sam iz filtera!");
	}

	@Override
	public void destroy() {
	}

}