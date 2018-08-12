package org.lots.candy.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.domain.TaskMapper;
import org.lots.candy.domain.UserMapper;
import org.lots.candy.entity.Action;
import org.lots.candy.entity.Task;
import org.lots.candy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("")
	public String init(Model model, HttpSession session){
		
		session.setAttribute(Constant.USER_SESSION_NAME, "admin");
		
		// 正文开始
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		if (!Constant.ADMIN.equals(userId)) {
			return "redirect:/";
		}
		User user = userMapper.findUserByElement("userId", userId);
		
		model.addAttribute("user", user);
		List<Task> taskList = taskMapper.queryValidateTask();
		
		model.addAttribute("taskList", taskList);
		
		
		
		return "admin/index";
	}
	@RequestMapping("/queryAction")
	@ResponseBody
	public Map query(HttpServletRequest request){
		String pageNoStr = request.getParameter("pageNo");
		int pageno = 0;
		if(pageNoStr == null || "".equals(pageNoStr)){
			pageno = 1;
		}else{
			pageno = Integer.parseInt(pageNoStr);
		}
		int pageSize = 10;
		
		int start = (pageno - 1) * pageSize;
				
		List<Action> actions = taskMapper.queryActions(request.getParameter("userName"), request.getParameter("task"), request.getParameter("ifHandled"), start, pageSize);
		
		int totalSize =  taskMapper.queryActionsCount(request.getParameter("userName"), request.getParameter("task"), request.getParameter("ifHandled"));
		int totalPageCount = (totalSize + pageSize - 1) / pageSize;
		
		Map result = new HashMap();
		result.put("result", actions);
		result.put("totalPageCount", totalPageCount);
		return result;
	}
	@RequestMapping("/updateAction")
	@ResponseBody
	public String updateAction(HttpServletRequest request){
		String ids = request.getParameter("ids");
		List<String> idList = Arrays.asList(ids.split(","));
		String point = request.getParameter("point");
		String ifEffective = request.getParameter("ifEffective");
		
		taskMapper.updateAction(idList, point, ifEffective);
		return "success";
	}

}
