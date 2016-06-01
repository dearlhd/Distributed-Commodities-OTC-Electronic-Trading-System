package com.trader.utils.httpClient;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	public HttpClientUtil() {
		
	}

	public JSONObject postMessage(String url, JSONObject obj) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postMethod = new HttpPost(url);

			StringEntity entity = new StringEntity(obj.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			postMethod.setEntity(entity);

			HttpResponse result = httpClient.execute(postMethod);
			String resData = EntityUtils.toString(result.getEntity());

			obj = JSONObject.fromObject(resData);
			System.out.println("client: " + obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
