define(function(require, exports, module) {
  // 模块代码
	var $ = require("jquery");
	
	module.exports = {
		init: function(){
			var self = this;
			$(".js_showUserFloat").click(function() {
		        $('.js_userFloat').toggle();
		    });
			
			$(".js_logout").click(function() {
		    	window.location="/logout";
		    });
			
			self._initPwd();
		
		},
		_closeModal: function(){
			$(".modal").hide();
			$(".js_modal_mask").hide();
		},
		_initPwd: function(){
			var self = this;
			
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
		_initDatagrid:  function(){
			var self = this;
			$(".task-container").datagrid({
				actions : [{
					name : query,
					label : "query",
					url: "/admin/queryAction",
					type : page,//check触发checkbox, page触发page动作, none
		        }],
			});
		},
	};
	
	
});