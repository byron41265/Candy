package org.lots.candy.web;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.lots.candy.config.Constant;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.User;
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
	
	@Value("${spring.mail.url}")
	private String emailUrl;
	
	@Value("${spring.mail.forget.url}")
	private String forgetUrl;
	
	@RequestMapping(value="/login" , method=RequestMethod.GET)
	public String initlogin(HttpSession session){
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		if (user != null){
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="/login" , method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpSession session){
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userMapper.findUserByEmailAndPwd(email, password);
		
		if(user!=null&&user.getStatus().equals("1")){
			session.setAttribute(Constant.USER_SESSION_NAME, user);
			return "success";
		}else if(user!=null&&user.getStatus().equals("0")){
			return "inactive";
		}else{
			return  "error";
		}
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
	public String register(HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String superInviteCode = request.getParameter("inviteCode");
		if(superInviteCode==null){
			superInviteCode="";
		}
		String userId = UUID.randomUUID().toString().replace("-", "");
		String inviteCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		String status = "0";
		if(userMapper.findUserByElement("username", username)!=null){
			return "nameError";
		}else if(userMapper.findUserByElement("email", email)!=null){
			return "emailError";
		}else if(superInviteCode!=null&&!superInviteCode.equals("")&&userMapper.findInviteCode(superInviteCode)==0){
			return "inviteCodeError";
		}
		userMapper.save(userId, username, password, email, inviteCode, superInviteCode, status);
		sendEmailUtils.sendRegisterUrl(username, email, emailUrl+userId);
		return "success";
	}
	
	@RequestMapping("/activeUser")
	public String updateUserStatus(HttpServletRequest request, Model model){
		String userId = request.getParameter("userId");
		userMapper.updateUserStatus(userId);
		model.addAttribute("msg", "You complete email verification.");
		return "msg";
	}
	
	@RequestMapping("/resetPwd")
	@ResponseBody
	public String resetPwd(HttpServletRequest request, HttpSession session){
		String old_pwd = request.getParameter("old_pwd");
		String new_pwd = request.getParameter("new_pwd");
		String new_pwd_again = request.getParameter("new_pwd_again");
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		String userId = user.getUserId();
		if(!new_pwd.equals(new_pwd_again)){
			return "againError";
		}else if(new_pwd.equals(old_pwd)){
			return "sameError";
		}else if(userMapper.findUserByEmailAndPwd(user.getEmail(), old_pwd) == null){
			return "pwdError";
		}else{
			userMapper.resetPassword(userId, new_pwd);
			return "success";
		}
	}
	
	@RequestMapping(value="/addWallet", method=RequestMethod.POST)
	@ResponseBody
	public String addWallet(HttpServletRequest request, HttpSession session){
		String wallet = request.getParameter("wallet");
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		String userId = user.getUserId();
		userMapper.addWallet(wallet, userId);
		return "success";
	}
	
	@RequestMapping(value="/logincheck", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String logincheck(Model model,HttpServletRequest request, HttpSession session) throws JSONException{
		JSONObject obj = new JSONObject();
		String errInfo = "";
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userMapper.findUserByEmailAndPwd(email, password);
		if(user!=null){			
			session.setAttribute(Constant.USER_SESSION_NAME, user);
			errInfo = "success";
		}else{
			errInfo = "uerror";
		}
		obj.put("result", errInfo);
//		obj.put("user", user);
		return obj.toString();
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
		sendEmailUtils.sendRestUrl(username, email, forgetUrl+userId);
		return "success";
	}
	
	@RequestMapping(value="/resetPage",method=RequestMethod.GET)
	public String enterReset(Model model, HttpServletRequest request){
		String userId = request.getParameter("userId");
		model.addAttribute("userId", userId);
		return "resetPwd";
	}
	
	@RequestMapping(value="/resetPwd",method=RequestMethod.POST)
	@ResponseBody
	public String resetPwd(HttpServletRequest request){
		String password = request.getParameter("password");
		String pwdAgain = request.getParameter("pwdAgain");
		String userId = request.getParameter("userId");
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
}
