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
		    $(".js_showBindWallet").click(function() {
		        $('.js_modal_bindWallet').toggle();
		        $('.js_modal_mask').toggle();
		    });
		    $(".js_showChangePwd").click(function() {
		        $('.js_modal_changePwd').toggle();
		        $('.js_modal_mask').toggle();
		    });
		    
		    $(".js_modal_mask").click(function() {
		        $('.js_modal_changePwd').hide();
		        $('.js_modal_bindWallet').hide();
		        $('.js_modal_completeTask').hide();
		        $(this).hide();
		    });
		    
		    self._initTask();
		},
		_initTask: function(){
			// 初始化提交链接的弹出框
			$(".js_showCmpleteTask").click(function() {
		        $('.js_modal_completeTask').toggle();
		        $('.js_modal_mask').toggle();
		    });
			
		}
	}

});