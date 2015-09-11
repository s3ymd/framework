package jp.co._3sss.web;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Router {

	private List<Route> routes = new LinkedList<Route>();

	public Route routing(HttpServletRequest request) {

		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		String path = uri.substring(contextPath.length() + 1);

		return routes.stream() //
				.filter(r -> r.matches(request, path)) //
				.findFirst() //
				.orElse(null);
	}

	public void addRoute(String urlPattern, String method, String controller, String action) {
		Route r = new Route();
		r.setUrlPattern(urlPattern);
		r.setControllerClassName(controller);
		r.setActionName(action);
		r.setHttpMethod(method);
		routes.add(r);
	}

}