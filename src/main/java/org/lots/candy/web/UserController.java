package org.lots.candy.web;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.lots.candy.config.Constant;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.User;
import org.lots.candy.utils.VerifyRecaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

@Controller
public class UserController {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SendEmailUtils sendEmailUtils;
	
	@Autowired
	DefaultKaptcha defaultKaptcha;
	
	@Value("${spring.mail.url}")
	private String emailUrl;
	
	@Value("${spring.mail.forget.url}")
	private String forgetUrl;
	
//	@Value("${spring.date}")
//	private String dateLimit;
	
	@RequestMapping(value="/login" , method=RequestMethod.GET)
	public String initlogin(HttpSession session){
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		if (userId != null && !"".equals(userId)){
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="/login" , method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpSession session) throws IOException,ParseException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifyCode");
		String captcha = (String)request.getSession().getAttribute("verifyCode");
		if((email==null||email.isEmpty())||(password==null||password.isEmpty())){
			return "Please enter your username and password";
		}
		User user = userMapper.findUserByEmailAndPwd(email, password);
		
		if(user!=null&&user.getStatus().equals("1")&&verifyCode.equals(captcha)){
			session.setAttribute(Constant.USER_SESSION_NAME, user.getUserId());
			
			logUserIpInfo(user.getUserId(), "login", request);
			return "success";
		}else if(user!=null&&user.getStatus().equals("0")){
			return "This user has not activated";
		}else if(!verifyCode.toUpperCase().equals(captcha.toUpperCase())){
			return "Verification code error";
		}else{
			return  "Email or password is error";
		}
	}
	
	private void logUserIpInfo(String userId, String op , HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		ip = ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
		String host = request.getRemoteHost();
	      int port = request.getRemotePort();
	      
	      userMapper.logUserIp(userId,ip,port,host,op);
	      
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(HttpServletRequest request, Model model){
		String inviteCode = request.getParameter("inviteCode");
		if(inviteCode!=null){
			model.addAttribute("inviteCode", inviteCode);
		}
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public String register(HttpServletRequest request) throws IOException{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifycode");
		String captcha = (String)request.getSession().getAttribute("verifyCode");
		//String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		//boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
		String email = request.getParameter("email");
		String superInviteCode = request.getParameter("inviteCode");
		int count = userMapper.findCodeTotalNum("superInviteCode");
		String message = null;
		if(count>=20){
			message = "The invitation code has been used more than 20 times and can no longer be used";
		}
//		if(superInviteCode==null){
//			superInviteCode="";
//		}
		String userId = UUID.randomUUID().toString().replace("-", "");
		Integer a = (int)((Math.random()*9+1)*1000);
		String b = a.toString();
		String parameter = userId+b;
		String inviteCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		String status = "0";
		if(userMapper.findUserByElement("username", username)!=null){
			message = "Username already exists";
		}else if(userMapper.findUserByElement("email", email)!=null){
			message = "Email has been registered";
		}else if(!"".equals(superInviteCode)&&superInviteCode!=null&&userMapper.findInviteCode(superInviteCode)==0){
			message = "The invitation code does not exist";
		}else if(!verifyCode.toUpperCase().equals(captcha.toUpperCase())){
			message = "Verification code error";
		}else{
			userMapper.save(userId, username, password, email, inviteCode, superInviteCode, status);
			sendEmailUtils.sendRegisterUrl(username, email, emailUrl+parameter);
			logUserIpInfo(userId, "register", request);
			message = "success";
		}
		return message;
	}
	
	@RequestMapping("/activeUser")
	public String updateUserStatus(HttpServletRequest request, Model model){
		String parameter = request.getParameter("parameter");
		String userId = parameter.substring(0, parameter.length()-4);
		userMapper.updateUserStatus(userId);
		userMapper.initUserTask(userId);
		model.addAttribute("msg", "Well done! You complete email verification.");
		return "msg";
	}
	
	@RequestMapping("/resetPassword")
	@ResponseBody
	public String resetPassword(HttpServletRequest request, HttpSession session){
		String old_pwd = request.getParameter("old_pwd");
		String new_pwd = request.getParameter("new_pwd");
		String new_pwd_again = request.getParameter("new_pwd_again");
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		User user = userMapper.findUserByElement("userId", userId);
		String message = null;
		if(!new_pwd.equals(new_pwd_again)){
			message = "Inconsistent input of new password twice";
		}else if(new_pwd.equals(old_pwd)){
			message = "The new password is the same as the old password";
		}else if(userMapper.findUserByEmailAndPwd(user.getEmail(), old_pwd) == null){
			message = "The original password was entered incorrectly";
		}else{
			userMapper.resetPassword(userId, new_pwd);
			message = "success";
		}
		return message;
	}
	
	@RequestMapping(value="/addWallet", method=RequestMethod.POST)
	@ResponseBody
	public String addWallet(HttpServletRequest request, HttpSession session){
		String wallet = request.getParameter("wallet");
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		userMapper.addWallet(wallet, userId);
		return "success";
	}
	

	
	@RequestMapping(value="/forgetPwd",method=RequestMethod.GET)
	public String forgetPwd(){
		return "forgetPwd";
	}
	
	@RequestMapping(value="/sendForgetEmail", method=RequestMethod.POST)
	@ResponseBody
	public String sendForgetEmail(HttpServletRequest request){
		String email = request.getParameter("email");
		User user = userMapper.findUserByElement("email", email);
		String username = user.getUsername();
		String userId = user.getUserId();
		Integer a = (int)((Math.random()*9+1)*1000);
		String b = a.toString();
		String parameter = userId+b;
		sendEmailUtils.sendRestUrl(username, email, forgetUrl+parameter);
		return "success";
	}
	
	@RequestMapping(value="/resetPage",method=RequestMethod.GET)
	public String enterReset(Model model, HttpServletRequest request){
		String parameter = request.getParameter("parameter");
		String userId = parameter.substring(0, parameter.length()-4);
		model.addAttribute("userId", userId);
		return "resetPwd";
	}
	
	@RequestMapping(value="/resetPwd",method=RequestMethod.POST)
	@ResponseBody
	public String resetPwd(HttpServletRequest request, HttpSession session){
		String password = request.getParameter("password");
		String pwdAgain = request.getParameter("pwdAgain");
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		if(!password.equals(pwdAgain)){
			return "equalError";
		}else{
			userMapper.resetPassword(userId, password);
			return "success";
		}
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(HttpSession session){
		session.removeAttribute(Constant.USER_SESSION_NAME);
		return "redirect:/login";
	}
	//生成验证码
	@RequestMapping(value="/defaultKaptcha",method=RequestMethod.GET)
	@ResponseBody
	 public void defaultKaptcha(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception{ 
	   byte[] captchaChallengeAsJpeg = null; 
	    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream(); 
	    try { 
	    //生产验证码字符串并保存到session中 
	    String createText = defaultKaptcha.createText(); 
	    httpServletRequest.getSession().setAttribute("verifyCode", createText); 
	    BufferedImage challenge = defaultKaptcha.createImage(createText); 
	    ImageIO.write(challenge, "jpg", jpegOutputStream); 
	    } catch (IllegalArgumentException e) { 
	     httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND); 
	     return; 
	    } 
	    captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
//	    BASE64Encoder encoder = new BASE64Encoder();
	    httpServletResponse.setHeader("Cache-Control", "no-store"); 
	    httpServletResponse.setHeader("Pragma", "no-cache"); 
	    httpServletResponse.setDateHeader("Expires", 0); 
	    httpServletResponse.setContentType("image/jpeg"); 
	    ServletOutputStream responseOutputStream = 
	      httpServletResponse.getOutputStream(); 
	    responseOutputStream.write(captchaChallengeAsJpeg); 
	    responseOutputStream.flush(); 
	    responseOutputStream.close(); 
	 }
	
}
