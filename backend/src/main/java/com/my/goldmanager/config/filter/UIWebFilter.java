package com.my.goldmanager.config.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UIWebFilter extends HttpFilter {
	private static final long serialVersionUID = 4632101869582420879L;

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String path = request.getRequestURI();
		if (!path.startsWith("/api") && !path.startsWith("/swagger-ui")
				&& !path.contains(".")) {

			request.getRequestDispatcher("/index.html").forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}
}
