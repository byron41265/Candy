define(function(require, exports, module) {
  // 模块代码
	var $ = require("jquery");
	
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
		    self._initSwitch();
		    self._initTask();
		    self._initPwd();
		    self._initWallet();
		    self._initRule();
		},
		_initSwitch: function(){
			$(".side_menu").children().each(function(){
		    	$(this).click(function(){
		    		$(this).addClass("active").siblings().removeClass("active");
		    		if($.trim($(this).text())=="Lots Credit"){
		    			$('.credit_table').show();
		    			$('.reward_table').hide();
		    		}else{
		    			$('.credit_table').hide();
		    			$('.reward_table').show();
		    		}
		    	});
		    });
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
		_initRule: function(){
			var self = this;
			$(".js_showRules").click(function() {
				var id = $(this).attr("data-id");
				var name = $(this).attr("data-name");
				$('.js_modal_rules .modal-row-title').html(name);
				$('.js_modal_rules .modal-row-content').html($("#js_hidden_rule .task-"+id).html());
				
		        $('.js_modal_rules').toggle();
		        $('.js_modal_mask').toggle();
		    });
		    $(".js_modal_rules .btn-submit ").click(function(){
		    	self._closeModal();
		    });
		    
		},
		_initPwd: function(){
			var self = this;
			$(".js_showBindWallet").click(function() {
		        $('.js_modal_bindWallet').toggle();
		        $('.js_modal_mask').toggle();
		    });
			
			$(".js_modal_changePwd form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							self._closeModal();
						}else{
							$.message('danger',result);
						}
					}
				}
			});
		},
		_initWallet: function(){
			var self = this;
			$(".js_showChangePwd").click(function() {
		        $('.js_modal_changePwd').toggle();
		        $('.js_modal_mask').toggle();
		    });
			
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