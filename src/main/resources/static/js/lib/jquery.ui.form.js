/**
 * created by xsm5202 2011-11-30
 *
 * Depends:
 * jquery.ui.core.js
 * jquery.ui.widget.js
 */
(function( $ , undefined) {


$.extend($.fn,{
	serializeTrim: function() {
		return $.param( this.serializeArrayTrim() );
	},

	serializeArrayTrim: function() {
		var rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
			rselectTextarea = /^(?:select|textarea)/i,
			rCRLF = /\r?\n/g;

		return this.map(function(){
			return this.elements ? jQuery.makeArray( this.elements ) : this;
		})
		.filter(function(){
			return this.name && !this.disabled &&
				( this.checked || rselectTextarea.test( this.nodeName ) ||
					rinput.test( this.type ) );
		})
		.map(function( i, elem ){
			var val = jQuery( this ).val();

			return val == null ?
				null :
				jQuery.isArray( val ) ?
					jQuery.map( val, function( val, i ){
						return { name: elem.name, value: val.replace( rCRLF, "\r\n" ).replace(/^\s+|\s+$/g, "") };
					}) :
					{ name: elem.name, value: val.replace( rCRLF, "\r\n" ).replace(/^\s+|\s+$/g, "") };
		}).get();
	}
});

var iconClass = {
	query : "fa fa-search",
	add : "fa fa-plus",
	edit : "fa fa-edit",
	"delete" : "fa fa-trash",
	reset : "fa fa-plug",
	save : "fa fa-save",
	submit : "fa fa-share",
	view : "fa fa-eye",
	review : "fa fa-check-square-o",
	audit : "fa fa-share-square-o",
	download : "fa fa-download",
	upload : "fa fa-upload",
	allsubmit : "fa fa-share",
	allreview : "fa fa-check-square-o"
};

var pattern = /#\{[\w.]+\}/g;

$.widget( "ui.form", {
	options : {
		action: {},
		type : "POST",
		data : null,
		layover : "body",//body
		validate : false
	},
	_create : function () {
		var form = this.element,
			options = this.options,
			layoverEle = $(form).closest(options.layover);

		if(layoverEle.length == 0) {
			layoverEle = $("body");
		}

		//initial url and type and
		var action=this.options.action;
		action.url =$(form).attr("action");

		$("a.form-submit", form).bind("click.form",function(e){
			//stop default submit action
			e.preventDefault();

			if(options.validate && !form.validate("validateForm")) {
				alert("Please fill the form correctly!");
				return;
			}
			//execute before submit function
			if(action.beforeSubmit && $.isFunction(action.beforeSubmit) && action.beforeSubmit.call(this, e)== false){
				return ;
			}

			layoverEle.layover();

			//set data
			var q = form.serializeTrim();
			
			

			if(options.type.toUpperCase() == "GET"){
				action.url += (action.url.indexOf('?') >= 0 ? '&' : '?') + q;
				action.data = null;  // data is null for 'get'
				action.type = "GET";
			}else {

				action.data = q; // data is the query string for 'post'
				action.type = "POST";
			}


			//set success callback function
			action.success = function (result, textStatus, jqXHR){
				layoverEle.layover("disable");

//					if(preHandleResult(result))
//						return ;



				var resetform;
				if($.isFunction(action.processResult)){
					resetform = action.processResult.call(this, result, textStatus, jqXHR);
				}

				if($.isFunction(action.afterProcessResult)) {
					action.afterProcessResult.call(this);
				}

				// reset all the input
				if(resetform ==null || resetform== false ){
					$("[type='text'], [type='password'], [type='file'], textarea", form).val("");
				}

			};
			action.error = function(jqXHR, textStatus, errorThrown){
				layoverEle.layover("disable");
				alert("System error!");
			};

			//ajax submit
			var fileInputs = $('input:file', form).length > 0;
			var mp = 'multipart/form-data';
			var multipart = (form.attr('enctype') == mp || form.attr('encoding') == mp);

			if(fileInputs && multipart) {
				//form.unbind();
				fileUpload();
			}else {
				$.ajax(action);
			}

			function fileUpload () {
				var id = 'jqFormIO' + (new Date().getTime()),
					iframeSrc = /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank',
					$io,io;

				$io = $('<iframe name="' + id + '" src="'+ iframeSrc +'" />');
				$io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });

				io = $io[0];

				form.attr("action",action.url);
				form.attr("method","POST");

				form.after($io);
				form.attr("target",id);
				io.attachEvent ? io.attachEvent('onload', cb) : io.addEventListener('load', cb, false);

				function cb(e) {
					io.detachEvent ? io.detachEvent('onload', cb) : io.removeEventListener('load', cb, false);
					//form.bind("submit.form",formSubmit);
					layoverEle.layover("disable");

					var doc = io.contentWindow ? io.contentWindow.document : io.contentDocument ? io.contentDocument : io.document;
						result = doc ? doc.body.innerHTML : '';

					if(preHandleResult(result) == false && $.isFunction(action.processResult)){
						action.processResult.call(this, result);
					}

					if($.isFunction(action.afterProcessResult)) {
						action.afterProcessResult.call(this);
					}
				}
				form.submit();
			}

		});

		//init validate component
		if(options.validate) {
			form.validate();
		}
	},
	destroy : function () {
		var form = this.element;
		$("a.form-submit",form).unbind("click.form");
	}
});

function getRealLength(value){
	//var pattern =/[\u4E00-\u9FA5\uf900-\ufa2d\uFE30-\uFFA0]/g;
	var pattern =/[^\u0000-\u00ff]/g;        //匹配双字节字符(包括汉字在内)
	return value.replace(pattern,'__').length;
};



$.widget( "ui.validate", {
	options : {
		rules : {},
		formulars:{},
		validators : {
			// http://docs.jquery.com/Plugins/Validation/Methods/required
			required : {
				message : "Required!",
				validate : function(value, element, param) {
					switch( element.nodeName.toLowerCase() ) {
						case 'select':
							// could be an array for select-multiple or a string, both are fine this way
							var val = $(element).val();
							return val && val.length > 0;
						case 'input':
							if ( /radio|checkbox/i.test(element.type) ) {
								// :TODO untested
								var form =  this.element,
									length = $("input[name='"+element.name+"']",form).filter(':checked').length ;
								return length > 0;
							}
						default :
							return $.trim(value).length > 0;
					}
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/email
			email: {
				message : "Please input a valid email!",
				validate : function(value, element) {
					// contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
					if(value == null || value.length == 0)
						return true;
					return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(value);
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/url
			url: {
				message : "Please input a valid url!",
				validate : function(value, element) {
					// contributed by Scott Gonzalez: http://projects.scottsplayground.com/iri/
					if(value == null || value.length == 0)
						return true;
					return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/date
			date: {
				message : "Please input a valid date!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return !/Invalid|NaN/.test(new Date(value));
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/dateISO
			dateISO: {
				message : "Please input a valid date,for example: 1987-11-19!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return /^\d{4}[-]\d{1,2}[-]\d{1,2}$/.test(value);
				}
			},

			phone: {
				message : "请输入有效的手机号码!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return /^\d{11}$/.test(value);
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/number
			number: {
				message : "请输入数值!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return  /^-?\d+(?:\.\d+)?$/.test(value);
					//return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
				}
			},
			posinumber: {
				message : "请输入非负数值!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return  /^\d+(?:\.\d+)?$/.test(value);
					//return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
				}
			},
			posinumberWeight: {
				message : "输入数值错误!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return  /^[0-9]{1,2}(?:\.\d{1,2})?$/.test(value);
					//return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
				}
			},
			posinumberTask: {
				message : "输入数值错误!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return  /^[0-9]{1,14}(?:\.\d{1,2})?$/.test(value);
					//return /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
				}
			},
			// http://docs.jquery.com/Plugins/Validation/Methods/digits
			digits: {
				message : "请输入正整数!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return /^[1-9]\d*$/.test(value);
				}
			},

			nonnegative: {
				message : "请输入非负整数!",
				validate : function(value, element) {
					if(value == null || value.length == 0)
						return true;
					return /^\d*$/.test(value);
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/minlength
			minlength : {
				message : "请输入不少于{0}个字节!",
				attrs : ["minlength"],
				validate : function(value, element, param) {
					return getRealLength(value) >= param[0];
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/maxlength
			maxlength : {
				message : "请输入不超过{0}个字节!",
				attrs : ["maxlength"],
				validate : function(value, element, param) {
					return getRealLength(value) <= param[0];
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/rangelength
			rangelength: {
				message : "请输入{0}到{1}个字节!",
				validate : function(value, element, param) {
					return  getRealLength(value) >= param[0] && getRealLength(value) <= param[1] ;
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/min
			min: {
				message : "请输入大于等于{0}的值!",
				validate : function( value, element, param ) {
					if(value == null || value.length == 0)
						return true;
					return value >= param;
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/max
			max: {
				message : "请输入小于等于{0}的值!",
				validate : function( value, element, param ) {
					if(value == null || value.length == 0)
						return true;
					return value <= param;
				}
			},

			// http://docs.jquery.com/Plugins/Validation/Methods/range
			range: {
				message : "请输入[{0},{1}]之间的值!",
				attrs : ["min","max"],
				validate : function( value, element, param ) {
					if(value == null || value.length == 0)
						return true;
					return value >= param[0] && value <= param[1] ;
				}
			},

			letternum : {
				message : "请输入字母,数字!",
				validate : function (value , element) {
					if(value == null || value.length == 0)
						return true;
					return /^[A-Za-z0-9]+$/.test(value);
				}
			},

			chletternum : {
				message : "请输入中文,字母,数字!",
				validate : function (value , element) {
					if(value == null || value.length == 0)
						return true;
					return /^[A-Za-z0-9\u4E00-\u9FA5\uf900-\ufa2d]+$/.test(value);
				}
			},

			letternumline : {
				message : "请输入字母,数字,下划线!",
				validate : function (value , element) {
					if(value == null || value.length == 0)
						return true;
					return /^\w+$/.test(value);
				}
			},
			chletternumline : {
				message : "请输入中文,字母,数字或下划线!",
				validate : function (value , element) {
					if(value == null || value.length == 0)
						return true;
					return /^[A-Za-z0-9_\u4E00-\u9FA5\uf900-\ufa2d]+$/.test(value);
				}
			},

			pattern : {
				message : "{1}",
				attrs : ["pattern","msg"],
				validate : function (value , element, params) {
					if(value == null || value.length == 0)
						return true;
					var pattern = new RegExp(params[0]);
					return pattern.test(value);
				}

			},
			math : {
				message : "{1}",
				attrs : ["math","mathmsg"],
				validate : function (value , element, params) {
					if(value == null || value.length == 0)
						return true;
					var form = this.element,
						formular;

					formular = params[0].replace(pattern,function(mat){
						var matchstr = mat.slice(2,-1);
						var tmp = $("[name='"+matchstr+"']",form).val();
						return (tmp != null) ? tmp : "";
					});

					try{
						var val = eval(formular);
						if ( val == true) {
							return true;
						}else {
							return false;
						}
					}catch(e){
						return true;
					}
				}
			},
			equal : {
				message : "请输入与{1}一致！",
				attrs : ["equal"],
				validate : function (value , element, params) {
					if(value == null || value.length == 0)
						return true;
					var equalEle = $("[name='"+params[0]+"']",this.element),
						equalVal = "";
					if (equalEle.length > 0) {
						this.validateEle(equalEle);
						equalVal = equalEle.val();
						if(equalVal==null||equalVal.length==0){
							return true;
						}
					}
					try{
						if(value == equalVal){
							return true;
						}else {
							var labelEle = equalEle.closest(".form-group").find(".control-label").first();
							params[1] = labelEle.length > 0 ? labelEle.text() : "";
							return false;
						}
					}catch(e){
						return false;
					}
				}
			},
			bigthan : {
				message : "请输入大于等于{1}的值！",
				attrs : ["bigthan"],
				validate : function (value , element, params) {
					if(value == null || value.length == 0)
						return true;
					var Ele = $("[name='"+params[0]+"']",this.element),
						Val = "";

					if (Ele.length > 0) {
						this.validateEle(Ele);
						Val = Ele.val();
						if(Val==null||Val.length==0){
							return true;
						}
					}

					try{
						if(parseFloat(value) >= parseFloat(Val)){
							return true;
						}else {
							var labelEle = Ele.closest(".form-group").find(".control-label").first();
							params[1] = labelEle.length > 0 ? labelEle.text() : "";
							return false;
						}
					}catch(e){
						return false;
					}
				}
			},
			smallthan : {
				message : "请输入小于等于{1}的值！",
				attrs : ["smallthan"],
				validate : function (value , element, params) {
					if(value == null || value.length == 0)
						return true;
					var Ele = $("[name='"+params[0]+"']",this.element),
						Val = "";
					if (Ele.length > 0) {
						this.validateEle(Ele);
						Val = Ele.val();
						if(Val==null||Val.length==0){
							return true;
						}
					}
					try{
						if(parseFloat(value) <= parseFloat(Val)){
							return true;
						}else {
							var labelEle = Ele.closest(".form-group").find(".control-label").first();
							params[1] = labelEle.length > 0 ? labelEle.text() : "";
							return false;
						}
					}catch(e){
						return false;
					}
				}
			},
			ipvalidate : {
				message : "请输入正确的IP，多个以英文逗号分隔!",
				validate : function (value , element) {
					if(value == null || value.length == 0)
						return true;
					var regexp=/^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;    //首先必须是xxx.xxx.xxx.xxx类型的数字
					for(var i=0;i<value.split(',').length;i++){					//多个ip以英文逗号分隔
						var ipVal=value.split(',')[i];
						if(!regexp.test(ipVal)){
							return false;
						};
						for(var j=0;j<ipVal.split('.').length;j++){						//每一段的值都不能大于255，并且不能以0打头
							var val=ipVal.split('.')[j];
							if(val.length>1&&val.charAt(0)=='0'){
								return false;
							}else if(parseInt(val,10)>255){
								return false;
							}
						}
					}
					return true;
				}
			}

		}
	},
	_create : function () {
		var self = this,
			form =  this.element,
			o = this.options,
			rules = o.rules,
			validators = o.validators,
			formulars = o.formulars;

		// 增加class rules
		$("[type='text'], [type='password'], [type='file'], textarea , select , [type='radio'], [type='checkbox']",form).each(function() {

			var eleRules = {},
				element = $(this),
				classes = element.attr('class');

			classes && $.each(classes.split(' '), function() {
				if (this in validators) {
					var validator = validators[this],
						newRule  = {},
						attrs = validator.attrs;
					if(attrs) {  //获取rule所需的属性参数,
						var params = [];
						for (var i = 0 ; i < attrs.length ; i++ ) {
							var attr = attrs[i];
								param = element.attr(attr);

							param && params.push(param);
						}
						newRule[this] = params;
					}else {

						newRule[this] = true;

					}
					$.extend(eleRules, newRule);

				}
			});

			rules[this.name] = eleRules;

			//新增计算规则
			if(element.attr('formular')){
				formulars[this.name] = element.attr('formular');
			}

		});

		//绑定文本框相应事件
		$("[type='text'], [type='password'], [type='file'], textarea  , select",form).off().on("focusin focusout",function(event){
			var target = event.target;
			var callback = self["_"+event.type];
			if($.isFunction(callback)){
				callback.call(self , target , event);
			}
		});

		$("[type='radio'], [type='checkbox']",form).on("click" , function(event) {
			var target = event.target;
			var callback = self["_"+event.type];
			if($.isFunction(callback)){
				callback.call(self , target , event);
			}
		});

	},
	_focusin : function (element, event) {
		this._hideError(element);
	},
	_focusout : function (element, event) {
		/*if(this.validateEle(element)){
			this.caculateEle();
		}*/
		this.validateEle(element);
	},
	_click : function (element , event ) {
		this.validateEle(element);
	},
	validateEle: function( element ) {
		var self = this ,
			o = this.options,
			eleRules = o.rules[element.name],
			validators = o.validators;

		for (var method in eleRules ) {
			var rule = { method: method, parameters: eleRules[method] };

			//调用验证方法

			var validateMethod = validators[method].validate;
			var result = validateMethod.call( self, element.value.replace(/\r/g, ""), element, rule.parameters );
			//若验证不通过
			if( !result ) {

				//获取错误提示信息
				var message = validators[method].message ,
					theregex = /\$?\{(\d+)\}/g;

				//若错误信息中包含变量用变量替换
				if (theregex.test(message)) {
					$.each(rule.parameters, function(i, n) {
						message = message.replace(new RegExp("\\{" + i + "\\}", "g"), n);
					});
				}

				//显示错误信息
				self._showError(element,message);
				return false;
			}
		}
		self._showCorrect(element);
		return true;
	},
	caculateEle : function(){
		var form = this.element,
			o = this.options,
			formulars = o.formulars,
			key = null,
			formular;
		for (key in formulars){
			formular = formulars[key];
			formular = formular.replace(pattern,function(mat){
				var matchstr = mat.slice(2,-1);
				var tmp = $("[name='"+matchstr+"']",form).val();
				return (tmp != null) ? tmp : "";
			});
			try{
				var val = eval(formular);
				if ( val == Infinity||val == -Infinity||isNaN(val)) {
					$("[name='"+key+"']",form).val("");
				}else {
					val=val.toFixed(4);
					val=new Number(val);
					$("[name='"+key+"']",form).val(val);
				}

			}catch(e){
				$("[name='"+key+"']",form).val("");
			}
		}
	},
	_showCorrect : function(element ){
		//对于checkbox,radiobox作特殊处理
		if (/radio|checkbox/i.test(element.type)){
			element = element.parentNode.parentNode;
		}

		$(element).closest(".input-wrap").removeClass("icon-right icon-error").addClass("icon-right");

		var $span = $(element).nextAll("div.error-msg");
		$span.removeClass("show").addClass("hide");
		$span.html("");
	}, 
	_showError : function (element , message) {
		//对于checkbox,radiobox作特殊处理
		if (/radio|checkbox/i.test(element.type)){
			element = element.parentNode.parentNode;
		}

		$(element).closest(".input-wrap").addClass("icon-error");

		var $span = $(element).nextAll("div.error-msg");
		if($span.length > 0) {
			$span.removeClass("hide").addClass("show");
			$span.html(message);
		}else {
			$("<div class='error-msg ui-validate-error help-block show'>"+message+"</div>").insertAfter(element);
		}
	},
	_hideError : function (element) {
		//对于checkbox,radiobox作特殊处理
		if (/radio|checkbox/i.test(element.type)){
			element = element.parentNode.parentNode;
		}

		$(element).closest(".input-wrap").removeClass("icon-right icon-error");

		var $span = $(element).nextAll("div.error-msg");
		$span.removeClass("show").addClass("hide");
		$span.html("");
	},
	validateForm : function () {
		var self = this ,
			$ele = this.element;
		if($ele.length > 0) {
			var form = $ele[0],
				elements = form.elements;

			for (var i = 0 ; i < elements.length ; i++) {
				if(!self.validateEle(elements[i])){
					return false;
				}
			}
			$(".icon-right", form).removeClass("icon-right");
			
			return true;
		}
		return false;
	}
});

var layoverId = 0;

function getNextLayOverId() {
	return ++layoverId;
}

$.widget( "ui.layover",{
	options : {
		id : "layover-"
	},
	_create : function (){
		var o = this.options,
			layover = $(".ui-layover",this.element);

		if(layover.length == 0){
			layover = $("<div id='"+o.id+getNextLayOverId()+"' class='ui-layover'></div>");
			this.element.append(layover);
		}

	},
	_init : function () {
		var layover = $(".ui-layover",this.element);

		if (layover.length > 0){
			layover.css({"opacity":"0.3","display":"block"})
				.offset(this.element.offset())
				.width(this.element.width())
				.height(this.element.height());
		}
	},
	disable : function () {
		var layover = $(".ui-layover",this.element);
		if (layover.length > 0){
			layover.css("display","none");
		}
	},
	enable : function () {
		var layover = $(".ui-layover",this.element);
		if (layover.length > 0){
			layover.css("display","block");
		}
	},
	destroy : function (){
		var layover = $(".ui-layover",this.element);

		if(layover.length > 0){
			layover.remove();
		}
	}
});




function generatePagination() {
	var self = this ,
		o = this.options,
		pageOption = o.pageOption,
		table = this.element;

	//Step 0 : clear old pagination
	var prePageDiv = $("tfoot div.ui-page",table);
	if( prePageDiv.length > 0) {
		$("a",prePageDiv).unbind();
		$("select",prePageDiv).unbind();
		prePageDiv.remove();
	}

	//Step 1 :count display pageNo interval
	var displayEntriesNum = pageOption.displayEntriesNum;
	var halfDisplayEntriesNum = Math.ceil(displayEntriesNum/2);
	var totalPageNum = pageOption.totalPageNum ;
	var upperLimit = totalPageNum-displayEntriesNum;
	var currentPageNo = pageOption.currentPageNo;
	var start = currentPageNo > halfDisplayEntriesNum ? Math.max(Math.min(currentPageNo - halfDisplayEntriesNum, upperLimit), 1):1;
	var end = currentPageNo > halfDisplayEntriesNum ? Math.min( currentPageNo + halfDisplayEntriesNum, totalPageNum) : Math.min(displayEntriesNum, totalPageNum);

	//Step 2: generate anchors
	var pageDiv = $("<div class='ui-page'></div>");

	function generateItem(pageNo , anchorText , anchorClass) {
		var a;
		pageNo = pageNo < 1 ? 1 :( pageNo > totalPageNum ? totalPageNum : pageNo);// pre and next
		if ( pageNo == currentPageNo && anchorClass == null) {
			a = $("<span class='ui-page-current'>"+anchorText+"</span>");
		}else if( pageNo == currentPageNo && anchorClass != null ) {
			a = $("<span class='ui-page-disable'>"+anchorText+"</span>");
		}else {
			a = $("<a href='#'>"+anchorText+"</a>").bind("click.page",function (e) {
				//:TODO bind click event
				self.showPage(pageNo);
			});
		}
		if(anchorClass) {
			a.addClass(anchorClass);
		}
		pageDiv.append(a);
	}

	//generate first anchor
	generateItem(1 , pageOption.firstAnchorText,"ui-page-first");
	//generate previous anchor
	generateItem(currentPageNo - 1 , pageOption.previousAnchorText,"ui-page-prev");

	var i;
	if(start > 1 && pageOption.edgeEntriesNum > 0 ){
		var edgeEnd = Math.min(pageOption.edgeEntriesNum , start);
		for ( i = 1 ; i <= edgeEnd ; i++ ){
			generateItem( i , i );
		}
		if(i < start){
			pageDiv.append("<span>...</span>");
		}
	}

	//generate page
	for(  i = start; i <= end; i++ ){
		generateItem( i , i );
	}

	if(end < totalPageNum && pageOption.edgeEntriesNum > 0 ) {
		if( end < totalPageNum - pageOption.edgeEntriesNum) {
			pageDiv.append("<span>...</span>");
		}
		var edgeBegin = Math.max(totalPageNum - pageOption.edgeEntriesNum + 1, end);
		for( i = edgeBegin ; i <= totalPageNum ; i++) {
			generateItem( i , i );
		}
	}

	//generate next anchor
	generateItem(currentPageNo + 1 , pageOption.nextAnchorText , "ui-page-next");
	//generate last anchor
	generateItem(totalPageNum , pageOption.lastAnchorText , "ui-page-last");

	//添加页面跳转控件
	var pageSelect = $("<select>");

	for ( i = 1 ; i <= totalPageNum ; i++){
		if( i == currentPageNo) {
			pageSelect.append("<option selected='selected'>"+i+"</option>");
		}
		else {
			pageSelect.append("<option>"+i+"</option>");
		}
	}

	pageSelect.bind("change.page",function(){
		self.showPage(parseInt($(this).val()));
	});

	pageDiv.append("<span>跳转至第</span>").append(pageSelect).append("<span>页</span>");
	$("tfoot td",table).append(pageDiv);
}

function generateRow(data , rtmpl , rhandler,subStr,colspanSize) {
	if(rhandler && $.isFunction(rhandler)) {
		return rhandler(data);
	}else if(rtmpl) {
		return rtmpl.replace(pattern,function(mat){
			var matchstr = mat.slice(2,-1);
			var tmp = data,
				sp = matchstr.split(".");
			for(var i = 0 ; tmp && i < sp.length ; i++) {
				var spstr =  sp[i];
				tmp = tmp[spstr];
			}
			tmp=(tmp != null) ?tmp:"";
			if(subStr && tmp.length>subStr){				//如果字符太长，显示点点点，进行截取字符串
				tmp=tmp.substring(0,subStr)+"...";
			}
			//tmp.replace(/</g,"&lt;") /</g是正则表达式的写法 可以实现replaceAll 王阿强增加 2015年11月10日14:02:59 原因：解决结果中含有html代码，页面显示html代码转换后的情况
			//比如: <html><input type="button">ff</input></html>  前端只输出ff的情况
			tmp=(tmp + "").replace(/</g,"&lt;").replace(/>/g,"&gt;");
			return tmp;
		});
	} else {
		return "<tr><td colspan='"+colspanSize+"'></td></tr>";
	}
}

$.widget( "ui.page",{
	options : {
		headTemplate : null ,
		rowTemplate : null ,
		rowHandler : null,
		oddRowClass : null,
		evenRowClass : null,
		footTemplate : "",
		// page object contains data info and page info
		page : null,
		//used for generate page url
		url : null,
		data : null,
		check : true,
		subStr: null,
		pageOption : {
			totalPageNum : null , //总页面数
			currentPageNo : 1 , //当前所在页面
			pageSize : 15 , //record number displayed per page
			displayEntriesNum : 3 , //main display page num
			edgeEntriesNum : 1 ,//edge display page num
			firstAnchorText : "首页",
			previousAnchorText : "上一页",
			nextAnchorText : "下一页",
			lastAnchorText : "末页"
		},
		beforeShowPage : function(){
			var winWidth = $(window).width();
			$(".maincontent").width(winWidth - 240);
			$(".header").width(winWidth);

			var winSize = $(window).height();
			$('.rightpanel').height(winSize-110);
		},
		afterShowPage : function(){
			//目前仅修复右边panel的高度，与功能无关
			var winWidth = $(window).width();
			var tableWidth = $(this).width();
			if( winWidth - 280 < tableWidth){
				$(".maincontent").width(tableWidth + 40);
				$(".header").width(tableWidth + 280);
			}

			var winSize = $(window).height();
			if($('.maincontent').height() < winSize - 110) {
				$('.rightpanel').height(winSize-110); //rightpanel = window高度 - header高度
			}else {
				$('.rightpanel').height($('.maincontent').height());
			}
		}
	},
	_create : function () {
		var table = this.element;
		if($("thead",table).length == 0) {
			table.append("<thead></thead>");
		}
		if($("tbody",table).length == 0) {
			table.append("<tbody></tbody>");
		}
		if($("tfoot",table).length == 0) {
			var colspanSize=$("thead",table).find("th").length;
			if(colspanSize==0){
				colspanSize=100;
			}
			table.append("<tfoot><tr><td colspan='"+colspanSize+"'></td></tr></tfoot>");
		}
	},
	_init : function (){
		var o = this.options,
			table = this.element,
			check = o.check;

		if(o.headTemplate) {
			$("thead",table).html(o.headTemplate);
		}

		if(check){
			$("thead",table).find("input[type='checkbox']").click(function(e){
				var checkbo = $(this).prop('checked');

				$("tbody",table).find("input[type='checkbox']").each(function(i){
					$(this).attr("checked",checkbo);
				});
			});
		}

	},
	//construct tbody and tfooter using page object
	showPage : function (currentPageNo , pageData , url , data){
		var self = this ,
			o = this.options,
			table = this.element,
			rtmpl = o.rowTemplate,
			rhandler = o.rowHandler,
			oClass = o.oddRowClass,
			eClass = o.evenRowClass,
			check = o.check,
			page = null;

		o.page = null;
		if(url) {
			o.url = url;
			o.data = data;
		}
		if(pageData == null && o.url){
			// :TODO set layover
			pageData = $.ajax({
				url : o.url + (o.url.indexOf('?') >= 0 ? '&' : '?')+"pageNo="+currentPageNo ,
				data : o.data ,
				type : "POST" ,
				async: false,
				error : function() {
					alert("获取数据请求失败!");
				}
			}).responseText;
		}

		if(pageData) {

			if(preHandleResult(pageData))
				return ;

			try {
				page = $.parseJSON(pageData);
				//将page对象放到option中,添加对查询结果为空的验证
				o.page = page;
			}catch(e) {
				alert("数据解析失败!");
				return ;
			}
		}else {
			return ;
		}

		//Step 1. construct tbody
		var body = $("tbody",table);

		body.empty();

		if(check) {
			$("thead",table).find("input[type='checkbox']").attr("checked",false);
		}

		var colspanSize=$("thead",table).find("th").length;
		if(colspanSize==0){
			colspanSize=100;
		}

		if(page.rowTemplate){
			rtmpl = page.rowTemplate;
		}

		self._trigger("beforeShowPage");

		if(page.result.length==0){						//add by wangaq 2015年11月25日9:45:47 增加如果没有记录则显示 查询无结果
			body.append("<tr><td colspan='"+colspanSize+"'>查询无结果</td></tr>");
		}

		$.each(page.result, function( i , data ){
			//substitute variable for rowTemplate
			var row = generateRow(data , rtmpl , rhandler,o.subStr,colspanSize);

			var tr = $(row);

			if(check){
				tr.prepend('<td><input type="checkbox" value="'+i+'"></td>');
			}

			//add evenRowClass
			if(oClass && i%2 == 0){
				tr.addClass(oClass);
			}
			if(eClass && i%2 == 1){
				tr.addClass(eClass);
			}
			body.append(tr);
		});

		//Step 2. create pagination
		o.pageOption.totalPageNum = page.totalPageCount;
		if(currentPageNo > 0 && currentPageNo <= o.pageOption.totalPageNum) {
			o.pageOption.currentPageNo = currentPageNo;
		}else {
			return ;
		}
		generatePagination.call(self);

		self._trigger("afterShowPage");

	},
	getChecked : function(){
		var table = this.element,
			body = $("tbody",table),
			result = [],
			page = this.options.page;

		body.find("input:checked").each(function(i){
			var index = $(this).val(),
				data = page.result[index];
			data._pageIndex = i;
			result.push(data);
		});
		return result;
	}
});

$.widget( "ui.pagination",{
	options : {
		totalPageNum : null , //总页面数
		currentPageNo : 1 , //当前所在页面
		pageSize : 15 , //record number displayed per page
		displayEntriesNum : 3 , //main display page num
		edgeEntriesNum : 1 ,//edge display page num
		firstAnchorText : "首页",
		previousAnchorText : "上一页",
		nextAnchorText : "下一页",
		lastAnchorText : "末页",
		showPage : function ( pageNo ) {

		}
	},
	_init : function (){
		var self = this ;

		self._generatePagination();
	},
	_generatePagination : function () {
		var o = this.options,
			container = this.element;

		//清理上次分页信息
		$("a",container).unbind("click.page");
		container.empty();

		//Step 1 :count display pageNo interval
		var displayEntriesNum = o.displayEntriesNum;
		var halfDisplayEntriesNum = Math.ceil(displayEntriesNum/2);
		var totalPageNum = o.totalPageNum ;
		var upperLimit = totalPageNum-displayEntriesNum;
		var currentPageNo = o.currentPageNo;
		var start = currentPageNo > halfDisplayEntriesNum ? Math.max(Math.min(currentPageNo - halfDisplayEntriesNum, upperLimit), 1):1;
		var end = currentPageNo > halfDisplayEntriesNum ? Math.min( currentPageNo + halfDisplayEntriesNum, totalPageNum) : Math.min(displayEntriesNum, totalPageNum);

		container.addClass("ui-page");

		function generateItem(pageNo , anchorText , anchorClass) {
			var a;
			pageNo = pageNo < 1 ? 1 :( pageNo > totalPageNum ? totalPageNum : pageNo);// pre and next
			if ( pageNo == currentPageNo) {
				a = $("<span class='ui-page-current'>"+anchorText+"</span>");
			}else {
				a = $("<a href='#'>"+anchorText+"</a>").bind("click.page",function (e) {
					o.showPage(pageNo);
				});
			}
			if(anchorClass) {
				a.addClass(anchorClass);
			}
			container.append(a);
		}

		//generate first anchor
		//generateItem(1 , o.firstAnchorText,"ui-page-first");
		//generate previous anchor
		//generateItem(currentPageNo - 1 , o.previousAnchorText,"ui-page-prev");

		var i;
		if(start > 1 && o.edgeEntriesNum > 0 ){
			var edgeEnd = Math.min(o.edgeEntriesNum , start);
			for ( i = 1 ; i <= edgeEnd ; i++ ){
				generateItem( i , i );
			}
			if(i < start){
				container.append("<span>...</span>");
			}
		}

		//generate page
		for(  i = start; i <= end; i++ ){
			generateItem( i , i );
		}

		if(end < totalPageNum && o.edgeEntriesNum > 0 ) {
			if( end < totalPageNum - o.edgeEntriesNum) {
				container.append("<span>...</span>");
			}
			var edgeBegin = Math.max(totalPageNum - o.edgeEntriesNum + 1, end);
			for( i = edgeBegin ; i <= totalPageNum ; i++) {
				generateItem( i , i );
			}
		}

		//generate next anchor
		//generateItem(currentPageNo + 1 , o.nextAnchorText , "ui-page-next");
		//generate last anchor
		//generateItem(totalPageNum , o.lastAnchorText , "ui-page-last");

	}
});



$.widget( "ui.datagrid",{
	options:{
		form : null,
		dataTable : null,
		actions : [{
			name : null,
			label : null,
			url: null,
			data:null,
			type:null,//check触发checkbox, page触发page动作, none
			process:null
        }],
        check : true,
        buttonAuths : null,
        pageOptions : null,
        subStr: null
	},
	_create : function(){
		var self = this,
			form = $("form",this.element),
			dataTable = $(".dataTable",this.element),
			thatElement = this.element,
			o = this.options,
			pageOptions = o.pageOptions;

		//创建建立表单按钮
		var $buttonGroup;
		if ($(".ui-datagrid-buttons",form).length == 0){
			var target = $("fieldset",form).length > 0 ? $("fieldset",form) : form;
			$buttonGroup = $("<div>").addClass("ui-datagrid-buttons");
			$buttonGroup.appendTo(target);
		}else {
			$buttonGroup = $(".ui-datagrid-buttons",form);
		}

		$.each(o.actions,function(index , action){
			if(o.buttonAuths && o.buttonAuths.indexOf(action.name+",") < 0){
				return ;
			}

			$icon = $("<span>").addClass(iconClass[action.name] ? iconClass[action.name] : "fa fa-search" );
			$anchor = $("<a>").addClass("btn btn-primary btn-small").css({"margin-right":"10px"}).append($icon).append(action.label).appendTo($buttonGroup);

			if (action.type == "page"){
				$anchor.bind("click.datagrid",function(e){
					e.preventDefault();
					if(action.beforeSubmit && $.isFunction(action.beforeSubmit) && action.beforeSubmit.call(this, e)== false){
						return ;
					}
					$(thatElement).layover();

					var ajaxOption = {};
					ajaxOption.url = action.url;
					ajaxOption.data = form.serializeTrim();
					ajaxOption.type = "POST";
					ajaxOption.success = function (result, textStatus, jqXHR){
						$(thatElement).layover("disable");
						if(preHandleResult(result) == false){
							dataTable.page("showPage",1,result,this.url,this.data);
						}
					};
					ajaxOption.error = function(jqXHR, textStatus, errorThrown){
						$(thatElement).layover("disable");
						alert("系统错误!");
					};

					$.ajax(ajaxOption);

				}).attr({"aria-type" : "page"});
			}else if (action.type == "check"){
				$anchor.bind("click.datagrid",function(e){
					e.preventDefault();
					var result = dataTable.page("getChecked");

					if(result.length == 0 ){
						alert("请至少选择一条记录");
						return ;
					}
					if(action.process && $.isFunction(action.process)){
						action.process.call(self,result);
					}
				});
			}else if (action.type == "checkpage"){
				$anchor.bind("click.datagrid",function(e){
					e.preventDefault();
					var result = dataTable.page("getChecked");

					if(result.length == 0 ){
						alert("请至少选择一条记录");
						return ;
					}
					if(action.process && $.isFunction(action.process)){
						action.process.call(self,result);
					}
					$(".ui-datagrid-buttons a[aria-type='page']",form).click();
				});
			}else if (action.type == "refresh"){
				$anchor.bind("click.datagrid",function(e){
					e.preventDefault();
					if(action.process && $.isFunction(action.process)){
						action.process.call(self,null);
					}
					$(".ui-datagrid-buttons a[aria-type='page']",form).click();
				});
			}else {
				$anchor.bind("click.datagrid",function(e){
					e.preventDefault();
					if(action.process && $.isFunction(action.process)){
						action.process.call(self,null);
					}
				});
			}

		});

		//初始化page
		dataTable.page(pageOptions);
	}
});


})(jQuery);



