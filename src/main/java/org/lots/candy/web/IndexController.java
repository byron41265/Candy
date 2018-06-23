package org.lots.candy.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.entity.Task;
import org.lots.candy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	@Autowired
	private TaskMapper taskMapper;
	
	
	@RequestMapping(value="/{providerId}", method=RequestMethod.GET)
	public String bindcallback(@PathVariable String providerId, Model model){
		
		if("twitter".equals(providerId)){
			Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter.class);
			if(connection != null){
				Twitter twitter = connection.getApi();
				System.out.println(twitter.userOperations().getUserProfile().getId());
				System.out.println(twitter.userOperations().getUserProfile().getName());
				System.out.println(twitter.userOperations().getUserProfile().getScreenName());
				model.addAttribute("bindmsg", "Twitter Bind Success");
			}
		}else if ("facebook".equals(providerId)){
			Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
			if(connection != null){
				Facebook facebook = connection.getApi();
				System.out.println(facebook.userOperations().getUserProfile().getId());
				System.out.println(facebook.userOperations().getUserProfile().getName());
				model.addAttribute("bindmsg", "FaceBook Bind Success");
			}
		}
	        
		return "index";
	}
	@RequestMapping("/")
	public String init(Model model, HttpSession session){
		
		// TO XUYUAN : 请将这段代码写入login 成功那段代码，这里仅做测试用，之后请帮忙删除
		User usertt = new User();
		usertt.setUserId("xsm");
		session.setAttribute(Constant.USER_SESSION_NAME, usertt);
		
		// 正文开始
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		
		List<Task> taskList = taskMapper.queryUserTask(user.getUserId());
		model.addAttribute("taskList", taskList);
		
		
		return "index";
	}

}
