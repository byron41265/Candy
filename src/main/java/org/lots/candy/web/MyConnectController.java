package org.lots.candy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.TaskEnum;
import org.lots.candy.entity.User;
import org.lots.candy.utils.FacebookHelper;
import org.lots.candy.utils.LinkedinHelper;
import org.lots.candy.utils.TwitterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

@Controller
public class MyConnectController extends ConnectController {
	
	private final UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	private String viewPath = "connect/";
	
	private ConnectionRepository connectionRepository;
	
	private ConnectionFactoryLocator connectionFactoryLocator;
	
	@Autowired
	private TwitterHelper twitterHelper;
	
	@Autowired
	private FacebookHelper facebookHelper;
	
	@Autowired
	private LinkedinHelper linkedinHelper;
	
	

	public MyConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.connectionRepository = connectionRepository;
		setApplicationUrl("https://candy.lots.org");
	}
	
	@RequestMapping(value="/{providerId}", method=RequestMethod.GET)
	public String connectionStatus(@PathVariable  String providerId, NativeWebRequest request, Model model) {
		setNoCache(request);
		processFlash(request, model);
		boolean flag = false;

		String userId = (String) request.getAttribute(Constant.USER_SESSION_NAME,	RequestAttributes.SCOPE_SESSION);

		
		if("twitter".equals(providerId)){
			Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
			if(connection != null){
				Twitter twitter = connection.getApi();
				twitterHelper.saveTwitterUser(userId, twitter);
				flag = true;
			}
		}else if ("facebook".equals(providerId)){
			Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
			if(connection != null){
				Facebook facebook = connection.getApi();
				facebookHelper.saveFacebookUser(userId, facebook);
				flag = true;
			}
		}else if ("linkedin".equals(providerId)){
			Connection<LinkedIn> connection = connectionRepository.findPrimaryConnection(LinkedIn.class);
			if(connection != null){
				LinkedIn linkedin = connection.getApi();
				linkedinHelper.saveLinkedinUser(userId, linkedin);
				flag = true;
			}
		}
		setNoCache(request);
		model.addAttribute("providerId", providerId);
		model.addAttribute("flag", flag);
		
		return getViewPath()+ "connectAll";			
	}
	
	
	
	
	
	
	public void setViewPath(String viewPath) {
		this.viewPath = viewPath;
	}
	
	private String getViewPath() {
		return viewPath;
	}
	
	private void processFlash(WebRequest request, Model model) {
		convertSessionAttributeToModelAttribute(DUPLICATE_CONNECTION_ATTRIBUTE, request, model);
		convertSessionAttributeToModelAttribute(PROVIDER_ERROR_ATTRIBUTE, request, model);
		model.addAttribute(AUTHORIZATION_ERROR_ATTRIBUTE, sessionStrategy.getAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE));
		sessionStrategy.removeAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE);
	}

	private void convertSessionAttributeToModelAttribute(String attributeName, WebRequest request, Model model) {
		if (sessionStrategy.getAttribute(request, attributeName) != null) {
			model.addAttribute(attributeName, Boolean.TRUE);
			sessionStrategy.removeAttribute(request, attributeName);
		}
	}

	private void setNoCache(NativeWebRequest request) {
		HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
		if (response != null) {
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 1L);
			response.setHeader("Cache-Control", "no-cache");
			response.addHeader("Cache-Control", "no-store");
		}
	}
	
	protected String getPathExtension(HttpServletRequest request) {
		String fileName = WebUtils.extractFullFilenameFromUrlPath(request.getRequestURI());		
		String extension = StringUtils.getFilenameExtension(fileName);
		return extension != null ? "." + extension : "";
	}
	
	protected boolean prependServletPath(HttpServletRequest request) {
		return !this.urlPathHelper.getPathWithinServletMapping(request).equals("");
	}
	
	protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		String path = "/connect/" + providerId + getPathExtension(servletRequest);
		if (prependServletPath(servletRequest)) {
			path = servletRequest.getServletPath() + path;
		}
		return new RedirectView(path, true, false);
	}
	
	
	
	

}
