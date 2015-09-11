package jp.co._3sss.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class Route {
	private String urlPattern;
	private String controllerClassName;
	private String actionName;
	private String httpMethod;
	private Pattern pattern;

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
		pattern = Pattern.compile(urlPattern);
	}

	public String getControllerClassName() {
		return controllerClassName;
	}

	public void setControllerClassName(String controller) {
		this.controllerClassName = controller;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String action) {
		this.actionName = action;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod.toLowerCase();
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public boolean matches(HttpServletRequest request, String path) {
		if (!request.getMethod().toLowerCase().equals(httpMethod)) {
			return false;
		}
		Matcher m = pattern.matcher(path);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

}