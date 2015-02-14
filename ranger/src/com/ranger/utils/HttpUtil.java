package com.ranger.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpUtil {
	private static final String TAG = "HttpUtil";

	// 返回数据给所调用的activty
	public static List<Object> getData(String _webUrl) {

		List<Object> items = new ArrayList<Object>();
		String data = "";
		String url = foramtStringForURLHttp(_webUrl);
		try {
			data = doGet(url);
			if (!"Get_error".equalsIgnoreCase(data) && data != null) {

				JSONObject json = new JSONObject(data);
				if (json.getInt("status") == 0)
					return items;

				JSONArray jsonArray = json.getJSONArray("result");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i); // 每条记录又由几个Object对象组成
					items.add(item);
				}

				// items = new Gson().fromJson(json.getString("result"),
				// new TypeToken<List<?>>() {
				// }.getType());
			} else {
				return items;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return items;
	}

	
	private static String foramtStringForURLHttp(String str) {
		return str.replace(" ", "%20");
	}
	
	// 返回数据给所调用的activty
	public static <T> List<T> getData(String _webUrl, Class<T> bean) {

		List<T> items = new ArrayList<T>();
		String data = "";
		String url =foramtStringForURLHttp(_webUrl);
		try {
			data = doGet(url);
			if (!"Get_error".equalsIgnoreCase(data) && data != null) {

				JSONObject json = new JSONObject(data);
				if (json.getInt("status") == 0)
					return items;
				JSONArray jsonArray = json.getJSONArray("result");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i); // 每条记录又由几个Object对象组成
					items.add((T) new Gson().fromJson(item.toString()
							.toLowerCase(), bean));
				}

				// items = new Gson().fromJson(json.getString("result"),
				// new TypeToken<List<?>>() {
				// }.getType());
			} else {
				return items;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return items;
	}

	public static String doGet(String path) throws Exception {
		Log.d(TAG, path);
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(6 * 1000);
			// 请求成功
			if (conn.getResponseCode() == 200) {
				// 获取服务器返回的数据

				byte[] data = readStream(conn.getInputStream());
				Log.i(TAG, new String(data, "UTF-8"));
				return new String(data, "UTF-8");
			} else {
				Log.i(TAG, "请求服务器出错!");
				return "Get_error";
			}
		} catch (Exception e) {
			return "Get_error";
		}

	}

	/*
	 * Function : 发送Post请求到服务器 Param : params请求体内容，encode编码格式 Author : 博客园-依旧淡然
	 */
	public static String doPost(String path, Map<String, String> params) {

		byte[] data = getRequestData(params, "utf-8").toString().getBytes(); // 获得请求体
		try {
			URL url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(6 * 1000); // 设置连接超时时间
			httpURLConnection.setDoInput(true); // 打开输入流，以便从服务器获取数据
			httpURLConnection.setDoOutput(true); // 打开输出流，以便向服务器提交数据
			httpURLConnection.setRequestMethod("POST"); // 设置以Post方式提交数据
			httpURLConnection.setUseCaches(false); // 使用Post方式不能使用缓存
			// 设置请求体的类型是文本类型
			httpURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 设置请求体的长度
			httpURLConnection.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			// 获得输出流，向服务器写入数据
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(data);

			int response = httpURLConnection.getResponseCode(); // 获得服务器的响应码
			if (response == HttpURLConnection.HTTP_OK) {
				InputStream inptStream = httpURLConnection.getInputStream();
				return dealResponseResult(inptStream); // 处理服务器的响应结果
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
	 */
	public static StringBuffer getRequestData(Map<String, String> params,
			String encode) {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), encode))
						.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	/*
	 * Function : 处理服务器的响应结果（将输入流转化成字符串） Param : inputStream服务器的响应输入流
	 */
	public static String dealResponseResult(InputStream inputStream) {
		String resultData = null; // 存储处理结果
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultData = new String(byteArrayOutputStream.toByteArray());
		return resultData;
	}

	// 获取输入流数据
	private static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	// 获取网络图片数据
	public static Bitmap returnBitMap(String path) {
		URL url = null;
		Bitmap bitmap = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream(); // 得到网络返回的输入流
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 提交参数里有文件的数据
	 * 
	 * @param url
	 *            服务器地址
	 * @param param
	 *            参数
	 * @return 服务器返回结果
	 * @throws Exception
	 */
	public static String uploadSubmit(String url, Map<String, String> param,
			File file) throws Exception {
		HttpPost post = new HttpPost(url);

		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),
							new StringBody(entry.getValue()));
				}
			}
		}
		// 添加文件参数
		if (file != null && file.exists()) {
			entity.addPart("file", new FileBody(file));
		}
		post.setEntity(entity);

		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					sb.append(tempLine);
				}
			}
		}
		post.abort();
		return sb.toString();
	}
	
	public static void CopyStream(String url, File f) {
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = getInputStream(url);
			byte[] data = new byte[1024];
			int len = 0;
			fileOutputStream = new FileOutputStream(f);
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static InputStream getInputStream(String path) {
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(3000);
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);// 表示从服务器获取数据
			urlConnection.connect();
			if (urlConnection.getResponseCode() == 200)
				return urlConnection.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
