package jp.co._3sss.web;


import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Dispatcher extends HttpServlet {
	private static final long serialVersionUID = 6706221145729143564L;
	private Router router;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			String routerClass = config.getInitParameter("router");
			router = (Router) Class.forName(routerClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String viewPrefix = "/WEB-INF/views/";
	private String viewSuffix = ".jsp";

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		try {
			Route route = router.routing(request);
			Class<? extends Controller> controllerClass = getControllerClass(route);
			Controller controller = controllerClass.newInstance();
			controller.setRequest(request);
			controller.setResponse(response);
			Method method = controllerClass.getMethod(route.getActionName());
			method.invoke(controller);
			String viewName = controller.getViewName();
			if (viewName != null) {
				request.getRequestDispatcher(viewPrefix + viewName + viewSuffix).forward(request, response);
			} else {
				response.sendRedirect(controller.getRedirectUrl());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Controller> getControllerClass(Route route) throws ClassNotFoundException {
		return (Class<? extends Controller>) Class.forName(route.getControllerClassName());
	}

}
