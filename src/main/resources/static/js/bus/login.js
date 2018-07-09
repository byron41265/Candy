define(function(require, exports, module) {
  // 模块代码
	var $ = require("jquery");
	
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
							$.message('danger',result);
							return true;
						}
						
					}
				}
				
			});
			
			
		}
	};
	
});
	