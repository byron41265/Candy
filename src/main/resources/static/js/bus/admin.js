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
			self._initDatagrid();
			self._initForm();
			
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
		_initForm: function(){
			var self = this;
			$(".js_modal_completeTask form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							self._closeModal();
							$(".task-container .ui-datagrid-buttons .query").click();
						}
					}
				}
			});
		},
		_initDatagrid:  function(){
			var self = this;
			$(".task-container").datagrid({
				actions : [{
					name : "query",
					label : "query",
					url: "/admin/queryAction",
					type : "page",//check触发checkbox, page触发page动作, none
		        },{
		        	name : "edit",
					label : "process",
					type : "check",
					process: function(dataList){
						var data = {}, flag = true, dataStr = '', i =0;
						
						for (i = 0 ; i < dataList.length; i++){
							data = dataList[i];
							dataStr = dataStr + data.id + ',';
							if(flag){
								$(".js_modal_completeTask select[name='point']").html("");
								if(data.taskEachPoint){
									$(".js_modal_completeTask select[name='point']").append("<option value='"+data.taskEachPoint+"'>"+data.taskEachPoint+"</option>");
								}
								if(data.taskEachPoint1){
									$(".js_modal_completeTask select[name='point']").append("<option value='"+data.taskEachPoint1+"'>"+data.taskEachPoint1+"</option>");
								}
								flag = false;
							}
						}
						
						$(".js_modal_completeTask input[name='ids']").val(dataStr);
						
						
						
						$('.js_modal_completeTask').toggle();
				        $('.js_modal_mask').toggle();
						
					}
		        }],
		        pageOptions: {
		        	rowTemplate: "<tr><td>#{userName}</td><td>#{taskName}</td><td>#{submitUrl}</td><td>#{submitTime}</td><td>#{ifHandled}</td><td>#{ifEffective}</td></tr>"
		        }
			});
			
			$(".task-container .ui-datagrid-buttons .query").click();
		},
	};
	
	
});