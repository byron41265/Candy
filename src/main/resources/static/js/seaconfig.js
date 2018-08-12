seajs.config({
	paths :{
		'lib': 'lib',
		'bus': 'bus',
	},
	alias: {
		'jquery': 'lib/lib.min.js',
		
		
		'index': 'bus/index',
		'login':  'bus/login',
		'register': 'bus/register',
		'forgetPwd': 'bus/forgetPwd',
		'resetPwd': 'bus/resetPwd',
		'admin': 'bus/admin',
	},
	preload:[
	    'jquery'
	],
	charset: 'utf-8'
});