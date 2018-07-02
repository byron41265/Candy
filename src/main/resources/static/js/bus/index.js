define(function(require, exports, module) {
  // 模块代码
	var $ = require("jquery");
	require("jquery-core");
	require("jquery-widget");
	require("jquery-form");
	
	module.exports= {
		init: function(){
			var self = this;
			$(".js_showNav").click(function() {
		        $('.js_mobile_nav').toggle();
		        $('.js_mask').toggle();
		    });
		    $(".js_mask").click(function() {
		        $(this).hide();
		        $('.js_modal').hide();
		    });
		    $(".js_showUserFloat").click(function() {
		        $('.js_userFloat').toggle();
		    });
		    $(".js_showBindWallet").click(function() {
		        $('.js_modal_bindWallet').toggle();
		        $('.js_modal_mask').toggle();
		    });
		    $(".js_showChangePwd").click(function() {
		        $('.js_modal_changePwd').toggle();
		        $('.js_modal_mask').toggle();
		    });
		    $(".js_logout").click(function() {
		    	window.location="/logout";
		    });
		    
		    $(".icon-link").click(function(){
		    	$('.js_modal_link').toggle();
		    	$('.js_modal_mask').toggle();
		    });
		    
		    $(".js_modal_mask").click(function() {
		        self._closeModal();
		    });		    
		    
		    self._initTask();
		    self._initPwd();
		    self._initWallet();
		},
		_closeModal: function(){
			$(".modal").hide();
			$(".js_modal_mask").hide();
		},
		// 初始化任务相关按钮
		_initTask: function(){
			// 初始化提交链接的弹出框
			var self = this;
			$(".js_showCmpleteTask").click(function() {
				// 修改任务参数
				var taskId = $(this).attr("data-task");
				$(".js_modal_completeTask input[name='taskId']").val(taskId);
				$(".js_modal_completeTask input[name='link']").val("");
				
				// 显示页面
		        $('.js_modal_completeTask').toggle();
		        $('.js_modal_mask').toggle();
		    });
			
			// 初始化弹出框的submit事件
			$(".js_modal_completeTask form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							self._closeModal();
						}
					}
				}
			});
			
		},
		
		_initPwd: function(){
			var self = this;
			$(".js_modal_changePwd form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							self._closeModal();
						}else if("againError" == result){
							alert("Inconsistent input of new password twice");
						}else if("sameError" == result){
							alert("The new password is the same as the old password");
						}else{
							alert("The original password was entered incorrectly");
						}
					}
				}
			});
		},
		_initWallet: function(){
			var self = this;
			$(".js_modal_bindWallet form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							self._closeModal();
						}
					}
				}
			});
		}
	}

});