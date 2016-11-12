package com.iwfu.drivertest.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iwfu on 2016/11/10 0010.
 * <p/>
 * 使用聚合数据API进行网络通信
 */
public class JuheUtils {
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "d22259057b16cac5488277e8f7a6b88b";

	// 1.题库接口
	public static Object getQuestion(int subject, String model, String testType) {
		String result;
		String url = "http://api2.juheapi.com/jztk/query";// 请求接口地址
		Map params = new HashMap();// 请求参数
		params.put("key", APPKEY);// 您申请的appKey
		params.put("subject", subject);// 选择考试科目类型，1：科目1；4：科目4
		params.put("model", model);// 驾照类型，可选择参数为：c1,c2,a1,a2,b1,b2；当subject=4时可省略
		params.put("testType", testType);// 测试类型，rand：随机测试（随机100个题目），order：顺序测试（所选科目全部题目）

		try {
			result = net(url, params, "GET");
			JSONObject object = new JSONObject(result);
			if (object.getInt("error_code") == 0) {
				Log.d("tag", "" + object.get("result"));
				return object.get("result"); // 直接返回json由Gson解析
			} else {
				System.out.println(object.get("error_code") + ":"
						+ object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 2.answer字段对应答案
	public static void getAnswer() {
		String result;
		String url = "http://api2.juheapi.com/jztk/answers";// 请求接口地址
		Map params = new HashMap();// 请求参数
		params.put("key", APPKEY);// 您申请的appk

		try {
			result = net(url, params, "GET");
			JSONObject object = new JSONObject(result);
			if (object.getInt("error_code") == 0) {
				Log.d("tag", "" + object.get("result"));
			} else {
				System.out.println(object.get("error_code") + ":"
						+ object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map params, String method)
			throws IOException {
		HttpURLConnection connection = null;
		String result = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}

			URL url = new URL(strUrl);
			connection = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				connection.setRequestMethod("GET");
			} else {
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
			}
			connection.setRequestProperty("User-agent", userAgent);
			connection.setConnectTimeout(DEF_CONN_TIMEOUT);
			connection.setReadTimeout(DEF_READ_TIMEOUT);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(false);
			connection.connect();
			if (params != null && method.equals("POST")) {
				DataOutputStream out = new DataOutputStream(
						connection.getOutputStream());
				out.writeBytes(urlencode(params));
			}
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			result = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return result;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=")
						.append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
