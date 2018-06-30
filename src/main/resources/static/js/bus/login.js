define(function(require, exports, module) {
  // 模块代码
	var $ = require("jquery");
	require("jquery-core");
	require("jquery-widget");
	require("jquery-form");
	
	module.exports= {
		init: function(){
			// 初始化checkbox
			$(".js_checkRem").click(function() {
		        $(this).toggleClass('checked');
		    });
			
			$("form").form({
				validate: true,
				action: {
					processResult: function(result){
						if("success" == result){
							window.location ="/";
						}else{
							alert("email or password is wrong");
						}
						
					}
				}
				
			});
			
			
		}
	};
	
});
	