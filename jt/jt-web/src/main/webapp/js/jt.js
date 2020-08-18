var TT = JT = {
	checkLogin : function(){
		//利用jQuery检查Cookie是否存在.
		var _ticket = $.cookie("JT_TICKET");
		if(!_ticket){
			return ; //如果不存在则直接返回
		}
		//当dataType类型为jsonp时，jQuery就会自动在请求链接上增加一个callback的参数
		$.ajax({
			url : "http://sso.jt.com/user/query/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					//和返回值的数据格式有关
					//SysResult sysResult = SysResult.success(data); //字符串当做参数返回
					//必须进行js对象转化
					//SysResult sysResult = SysResult.success(user); //对象当做参数返回
					//可以直接调用
					var _data = JSON.parse(data.data);
					var html =_data.username+"，欢迎来到京淘！<a href=\"http://www.jt.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});