package org.lots.candy.web;

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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("")
	public String init(Model model, HttpSession session){
		
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
	public Page query(HttpServletRequest request){
		String pageNoStr = request.getParameter("pageNo");
		int pageNo = 0;
		if(pageNoStr == null || "".equals(pageNoStr)){
			pageNo = 1;
		}else{
			pageNo = Integer.parseInt(pageNoStr);
		}
		
		Page<Action> actions = PageHelper.startPage(pageNo, 20).doSelectPage(() ->taskMapper.queryActions("", null, "N"));
		return actions;
	}

}
