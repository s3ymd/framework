package jp.co._3sss.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Session session;

	private String viewName;
	private String redirectUrl;

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		session = new Session(request.getSession());
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest request() {
		return request;
	}

	public HttpServletResponse response() {
		return response;
	}

	public Session session() {
		return session;
	}

	public String parameter(String parameterName) {
		return request.getParameter(parameterName);
	}

	public String[] parameterArray(String parameterName) {
		return request.getParameterValues(parameterName);
	}
	
	public void set(String name, Object value) {
		request.setAttribute(name, value);
	}

	public void render(String viewName) {
		this.viewName = viewName;
	}

	public void redirect(String url) {
		this.redirectUrl = url;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public String getViewName() {
		return viewName;
	}
}
