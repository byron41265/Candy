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
				$(this).removeClass("needcheck");
		        $(this).toggleClass('checked');
		    });
			
			$("form").form({
				validate: true,
				action: {
					beforeSubmit: function(){
						if($(".js_checkRem").hasClass("checked") == false){
							$(".js_checkRem").addClass("needcheck");
							return false;
						}else {
							return true;
						}
					},
					processResult: function(result){
						if("success" == result){
							alert("Please finish check you email to finish registration.");
							window.location ="/login";
						}else if("nameError" == result){
							alert("Username already exists.");
						}else if("emailError" == result){
							alert("Email has been registered.")
						}else if("outError" == result){
							alert("The invitation code has been used more than 20 times and can no longer be used");
						}else{
							alert("The invitation code does not exist")
						}
						return true;
					}
				}
				
			});
			
			
		}
	};
	
});
	