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
	
	@RequestMapping(value="/login" , method=RequestMethod.GET)
	public String initlogin(HttpSession session){
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		if (user != null){
			return "redirect:/index";
		}
		return "login";
	}
	
	@RequestMapping(value="/login" , method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpSession session){
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = userMapper.findUserByEmailAndPwd(email, password);
	
		if(user!=null){			
			session.setAttribute(Constant.USER_SESSION_NAME, user);
			return "success";
		}else{
			return  "error";
		}
	}
	
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	@RequestMapping("/uregister")
	public String register(HttpServletRequest request){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String userId = UUID.randomUUID().toString().replace("-", "");
		String status = "0";
		userMapper.save(userId, username, password, email, status);
		sendEmailUtils.sendRegisterUrl(email, "http://localhost:8080/activeUser?userId={userId}");
		return "login"; 
	}
	
	@RequestMapping("/activeUser")
	public String updateUserStatus(HttpServletRequest request){
		String userId = request.getParameter("userId");
		userMapper.updateUserStatus(userId);
		return "login";
	}
	
	@RequestMapping("/resetPwd")
	public void resetPwd(HttpServletRequest request, HttpSession session){
		String old_pwd = request.getParameter("old_pwd");
		String new_pwd = request.getParameter("new+pwd");
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		String userId = user.getUserId();
		if(userMapper.findUserByEmailAndPwd(user.getEmail(), old_pwd)!=null){
			userMapper.resetPassword(userId, new_pwd);
		}
	}
	
	@RequestMapping("/addWallet")
	public void addWallet(HttpServletRequest request, HttpSession session){
		String wallet = request.getParameter("wallet");
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		String userId = user.getUserId();
		userMapper.addWallet(wallet, userId);
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
	
	@RequestMapping("/getInfluenceReward")
	public void getInfluenceReward(HttpSession session){
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		
	}
}
