package org.lots.candy.web;

import javax.servlet.http.HttpSession;

import org.lots.candy.config.Constant;
import org.lots.candy.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@RequestMapping("/")
	public String init(Model model, HttpSession session){
		// TO XUYUAN : 请将这段代码写入login 成功那段代码，这里仅做测试用，之后请帮忙删除
//		User usertt = new User();
//		usertt.setUserId("admin");
		session.setAttribute(Constant.USER_SESSION_NAME, "admin");
		
		// 正文开始
		String userId = (String)session.getAttribute(Constant.USER_SESSION_NAME);
		
		return "admin/index";
	}

}
