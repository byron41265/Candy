package org.lots.candy.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.DictMapper;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.TwitterMapper;
import org.lots.candy.domain.UserMapper;
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
	
	@Autowired
	private UserMapper userMapper;
	@RequestMapping("/")
	public String init(Model model, HttpSession session){
		
		// TO XUYUAN : 请将这段代码写入login 成功那段代码，这里仅做测试用，之后请帮忙删除
		/*User user = new User();
		user.setUserId("xsm");
		user.setUsername("xsmxsm");
		user.setEmail("binggouxsm@hotmail.com");
		session.setAttribute(Constant.USER_SESSION_NAME, user);
		*/
		
		
		// 正文开始
		User user = (User)session.getAttribute(Constant.USER_SESSION_NAME);
		List<HashMap> pointList = this.getNextUserCount(user);
		model.addAttribute("pointList", pointList);
		
		int earned_point = userMapper.getPointByUserId(user.getUserId());
		model.addAttribute("earned_point",earned_point);
		// 获取列表
		List<Task> taskList = taskMapper.queryUserTask(user.getUserId());
		model.addAttribute("taskList", taskList);
		
		// 尚未转发的Tweet
		String retweetId = twitterMapper.getLatestUnRetweet();
		model.addAttribute("retweetId", retweetId);
		
		// 获取telegram 组链接
		List<Dict> groups = dictMapper.queryByType(Constant.SOCIAL_TELEGRAM_GROUPS);
		model.addAttribute("groups", groups);
		model.addAttribute("user", user);
		return "index";
	}
	
	public List<HashMap> getNextUserCount(User user){
		
		List<User> userList1 = userMapper.findUserBySuperInviteCode(user.getInviteCode());
		List<User> userList2 = new LinkedList<User>();
		List<User> userList3 = new LinkedList<User>();
		List<User> userList4 = new LinkedList<User>();
		List<User> userList5 = new LinkedList<User>();
		List<User> userList6 = new LinkedList<User>();
		List<User> userList7 = new LinkedList<User>();
		List<User> userList8 = new LinkedList<User>();
		HashMap map = new HashMap();
		for(User user1 : userList1){
			userList2.addAll(userMapper.findUserBySuperInviteCode(user1.getInviteCode()));
		}
		for(User user2 : userList2){
			userList3.addAll(userMapper.findUserBySuperInviteCode(user2.getInviteCode()));
		}
		for(User user3 : userList3){
			userList4.addAll(userMapper.findUserBySuperInviteCode(user3.getInviteCode()));
		}
		for(User user4 : userList4){
			userList5.addAll(userMapper.findUserBySuperInviteCode(user4.getInviteCode()));
		}
		for(User user5 : userList5){
			userList6.addAll(userMapper.findUserBySuperInviteCode(user5.getInviteCode()));
		}
		for(User user6 : userList6){
			userList7.addAll(userMapper.findUserBySuperInviteCode(user6.getInviteCode()));
		}
		for(User user7 : userList7){
			userList8.addAll(userMapper.findUserBySuperInviteCode(user7.getInviteCode()));
		}
		
		map.put(1, userList1.size());
		map.put(2, userList2.size());
		map.put(3, userList3.size());
		map.put(4, userList4.size());
		map.put(5, userList5.size());
		map.put(6, userList6.size());
		map.put(7, userList7.size());
		map.put(8, userList8.size());
		List<HashMap> list = userMapper.findInfluencePoint();
		for(Map map1 :list){
			int level = (int)map1.get("level");
			map1.put("people", map.get(level));
			int point = (int)map1.get("point");
			float rate = (float)map1.get("rate");
			int people = (int)map.get(level);
			map1.put("reward", point*rate*people);
		}
		return list;
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
