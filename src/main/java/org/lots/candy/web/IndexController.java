package org.lots.candy.web;

import java.util.List;







import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.DictMapper;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.TwitterMapper;
import org.lots.candy.entity.Dict;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
public class IndexController {
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private TwitterMapper twitterMapper;
	
	@Autowired
	private DictMapper dictMapper;
	
	@RequestMapping("/")
	public String init(Model model, HttpSession session){
		
		// TO XUYUAN : 请将这段代码写入login 成功那段代码，这里仅做测试用，之后请帮忙删除
		User usertt = new User();
		usertt.setUserId("xsm");
		session.setAttribute(Constant.USER_SESSION_NAME, usertt);
		
		// 正文开始
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		
		// 获取列表
		List<Task> taskList = taskMapper.queryUserTask(user.getUserId());
		model.addAttribute("taskList", taskList);
		
		// 尚未转发的Tweet
		String retweetId = twitterMapper.getLatestUnRetweet();
		model.addAttribute("retweetId", retweetId);
		
		// 获取telegram 组链接
		List<Dict> groups = dictMapper.queryByType(Constant.SOCIAL_TELEGRAM_GROUPS);
		model.addAttribute("groups", groups);
		
		return "index";
	}
	
	@RequestMapping(value="/link",  method=RequestMethod.POST)
	@ResponseBody
	public String submitLink(HttpServletRequest request,HttpSession session){
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		
		String userId = user.getUserId();
		request.getParameterMap();
		String link = request.getParameter("link");
		String taskId = request.getParameter("taskId");
		
		taskMapper.insertLinkAction(userId, link, taskId);
		
		return "success";
	}
	

}
