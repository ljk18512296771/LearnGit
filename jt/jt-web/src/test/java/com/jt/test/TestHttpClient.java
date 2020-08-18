package com.jt.test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

public class TestHttpClient {
	
	/**
	 * 案例说明:                           万能用法
	 * 	1.利用httpClient机制 访问百度服务器.   http://www.baidu.com
	 * 	2.实现步骤: (了解即可)
	 * 		1.定义请求网址
	 * 		2.定义httpClient工具API对象
	 * 		3.定义请求的类型
	 * 		4.发起请求,获取返回值结果
	 * 		5.校验返回值
	 * 		6.获取返回值结果数据.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		//String url = "http://www.baidu.com";//任意网络资源  包括业务服务器.
		String url = "http://manage.jt.com/web/testCors";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);//不能保证网络请求一定正确.
		//发起请求之后 需要判断返回值结果是否正确  一般条件下判断响应状态码信息是否为200.
		//404 400 提交参数异常  406 接收参数异常 500 服务器异常  504  超时   200正常 
		int status = response.getStatusLine().getStatusCode();
		if(status == 200) {
			//说明请求正确  获取返回值的实体对象
			HttpEntity httpEntity = response.getEntity();
			//将远程服务器返回的信息,转化为字符串. 方便调用   1.json   2.html代码片段
			String result = EntityUtils.toString(httpEntity,"utf-8");
			User user = ObjectMapperUtil.toObject(result, User.class);
			System.out.println(user);
		}
	}
	
	
	
	
	
	
}
