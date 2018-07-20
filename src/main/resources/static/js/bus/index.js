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
		    
		    self._initTask();
		    self._initPwd();
		    self._initWallet();
		    self._initRule();
		    self._initHighLight();
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
		},
		_initHighLight: function(){
			var self = this;
			$(".sidebar-section .sidebar-item, .game-container .game-item").click(function(e){
				var classes = $(this).attr("class");
				classes && $.each(classes.split(' '), function() {
					if (this.startsWith("item-")){
						var type = this.substring("item-".length);
						self.highLight(type);
					}
				});
			});
		},
		highLight: function(type){
			//清理样式
			$(".sidebar-section .sidebar-item").removeClass("active");
			$(".sidebar-section .sidebar-item.item-"+type).addClass("active");
			
			$(".credit_table .taskitem").removeClass("highlight");
			$(".credit_table .taskitem.taskType-"+type).addClass("highlight");
			
		}
	}

});