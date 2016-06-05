package com.trader.utils.httpClient;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.trader.utils.httpClient.SSLClient;

public class HttpClientUtil {
	public HttpClientUtil() {
		
	}

	public Object getMessageRetArray(String url) {
		try {
			System.out.println("trader post to " + url);
			HttpClient httpClient = new SSLClient();
			HttpGet getMethod = new HttpGet(url);


			HttpResponse response = httpClient.execute(getMethod);
			String resData = EntityUtils.toString(response.getEntity());
			
			if (resData == null) {
				return new JSONArray();
			}
			
			System.out.println("result from broker: " + resData);
			
			JSONArray resultArray = JSONArray.fromObject(resData); 
			System.out.println("trader recieve: " + resData);
			return resultArray;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public Object postMessageRetObject(String url, Object obj) {
		try {
			HttpClient httpClient = new SSLClient();
			HttpPost postMethod = new HttpPost(url);

			StringEntity entity = new StringEntity(obj.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			postMethod.setEntity(entity);

			HttpResponse result = httpClient.execute(postMethod);
			String resData = EntityUtils.toString(result.getEntity());
			
			JSONObject resultObject = JSONObject.fromObject(resData); 
			System.out.println("trader recieve: " + resData);
			return resultObject;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object postMessageRetArray(String url, Object obj) {
		try {
			System.out.println("trader post to " + url);
			System.out.println("post data: " + obj.toString());
			HttpClient httpClient = new SSLClient();
			HttpPost postMethod = new HttpPost(url);

			StringEntity entity = new StringEntity(obj.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			postMethod.setEntity(entity);

			HttpResponse result = httpClient.execute(postMethod);
			String resData = EntityUtils.toString(result.getEntity());
			
			if (resData == null) {
				return new JSONArray();
			}
			
			System.out.println("result from broker: " + resData);
			
			JSONArray resultArray = JSONArray.fromObject(resData); 
			System.out.println("trader recieve: " + resData);
			return resultArray;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
