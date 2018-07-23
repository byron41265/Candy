package org.lots.candy.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@WebFilter(urlPatterns = "/*")
@Order(value = 1)
public class LoginFilter implements Filter {

	
	static final Pattern ALLOWED_PATHS = Pattern.compile("(^/js/.*)|(^/css/.*)|(^/img/.*)|(^/fonts/.*)|(/favicon.ico)|(/index.html)|(/rule.html)|(/login)|(/register)|(/activeUser)|(/forgetPwd)|(/resetPwd)|(/resetPage)|(/sendForgetEmail)");

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String path = request.getRequestURI().substring(request.getContextPath().length())	.replaceAll("[/]+$", "");
		HttpSession session = request.getSession(true);
		Object userId = session.getAttribute(Constant.USER_SESSION_NAME);
		

		if (userId!=null) {
			chain.doFilter(req, res);
		} else if(ALLOWED_PATHS.matcher(path).matches()){
			chain.doFilter(req, res);
		}else{
			res.getOutputStream().print(redirectLogin());
			res.flushBuffer();
		}

	}
	
	public String redirectLogin(){
		StringBuffer out = new StringBuffer();
		out.append("<!DOCTYPE html>");
		out.append("<html lang=\"zh-CN\">");
		out.append("<body>");
		out.append("<script type=\"text/javascript\"> window.location=\"\\login\";</script>");
		out.append("</body>");
		out.append("</html>");
		return out.toString();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
