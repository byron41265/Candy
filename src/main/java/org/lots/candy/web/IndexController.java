package org.lots.candy.web;

import java.util.ArrayList;
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
import org.lots.candy.entity.TaskType;
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
		
		//session.setAttribute(Constant.USER_SESSION_NAME,"2fad339a599c46479bd7f24d2f3e7119");
		
		// 正文开始
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		if (Constant.ADMIN.equals(userId)) {
			return "redirect:/admin";
		}
		
		User user = userMapper.findUserByElement("userId", userId);
		// 获取邀请得分
		int influencedPeople = userMapper.findCodeTotalNum(user.getInviteCode());

		int eachPoint = taskMapper.getEachInvitePoint();
		int influencedPoint = eachPoint*influencedPeople;
//		int influencedPoint = 50*influencedPeople;
		taskMapper.insertTask(userId, "15", influencedPoint);
//		List<HashMap> pointList = this.getNextUserCount(user);
//		model.addAttribute("pointList", pointList);
		
		// TO XUYUAN : 请加一块根据userid 再查一次用户信息，我需要取到当前用户最新的bindaccount的情况，用于逻辑判断
		// 获取个人得分
		int earned_point = userMapper.getPointByUserId(userId);
		model.addAttribute("earned_point",earned_point);
		// 获取个人期望得分
		int rank = user.getRank();
		int expected_reward;
		if(rank<=100){
			expected_reward = userMapper.getRankTokens(rank);
		}else if(rank>100&&earned_point<1000){
			expected_reward = 0;
		}else{
			int usedReward = userMapper.getUsedReward();
			int peopleNum = userMapper.getPeopleNum();
			expected_reward = (2000000-usedReward)/peopleNum;
		}
		model.addAttribute("expected_reward", expected_reward);
		// 获取列表
		List<Task> taskList = taskMapper.queryUserTask(userId);
		model.addAttribute("taskList", taskList);
		
		List<TaskType> taskTypeList = taskMapper.queryUserTaskType(userId);
		model.addAttribute("taskTypeList", taskTypeList);
			
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
		
		List<HashMap> list = userMapper.findInfluencePoint();
		HashMap numMap = userMapper.findInfluencePeople(user.getUserId());
		
		for(Map map1 :list){
			int level = (int)map1.get("level");
			map1.put("people", numMap.get("num"+level));
			int point = (int)map1.get("point");
			float rate = (float)map1.get("rate");
			long people = (long)numMap.get("num"+level);
			map1.put("reward", point*rate*people);
		}
		return list;
	}
	
	@RequestMapping(value="/link",  method=RequestMethod.POST)
	@ResponseBody
	public String submitLink(HttpServletRequest request,HttpSession session){
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);

		request.getParameterMap();
		String link = request.getParameter("link");
		String taskId = request.getParameter("taskId");
		
		taskMapper.insertLinkAction(userId, link, taskId);
		
		return "success";
	}
	

}
