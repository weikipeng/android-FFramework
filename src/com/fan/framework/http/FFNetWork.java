package com.fan.framework.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;

import com.alibaba.fastjson.JSON;
import com.fan.framework.base.FFActivity;
import com.fan.framework.base.FFApplication;
import com.fan.framework.base.FFResponseCode;
import com.fan.framework.config.FFConfig;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;

public class FFNetWork {
	private static final String TAG = "FFNetWork";
	protected ExecutorService es1 = null;
	public final static ExecutorService es_all = Executors.newFixedThreadPool(6);
	private static HttpClient client = null;
	FFActivity activity;

	public FFNetWork(FFActivity activity) {
		this.activity = activity;
		es1 = Executors.newFixedThreadPool(5);
		if (client == null) {
			client = FFNetWorkUtils.getClient();
		}
	}
	
	public Object a(Class<?> aa) throws InstantiationException, IllegalAccessException{
		return aa.newInstance();
	}

	public <T> FFNetWorkRequest<T> get(String url, String words, FFNetWorkCallBack<T> callBack,Class<T> clazz,
			Object... params) {
		if(params != null && params.length%2==1){
			throw new RuntimeException("网络请求传入了单数个参数");
		}
		url = url + FFNetWorkUtils.getGetString(params);
		final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url, callBack,clazz, null, null, false);
		return request;
	}
	public <T> FFNetWorkRequest<T> get_synchronized(String url, String words, FFNetWorkCallBack<T> callBack,Class<T> clazz,
			Object... params) {
		if(params != null && params.length%2==1){
			throw new RuntimeException("网络请求传入了单数个参数");
		}
		url = url + FFNetWorkUtils.getGetString(params);
		final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url, callBack,clazz, null, null, true);
		return request;
	}

	public <T> FFNetWorkRequest<T> post(String url, String words, FFNetWorkCallBack<T> callBack,Class<T> clazz,
			Object... param) {
		if(param != null && param.length%2==1){
			throw new RuntimeException("网络请求传入了单数个参数");
		}
		final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url, callBack,clazz, null, param, false);
		return request; 
	}
	public <T> FFNetWorkRequest<T> post_synchronized(String url, String words, FFNetWorkCallBack<T> callBack,Class<T> clazz,
			Object... param) {
		if(param != null && param.length%2==1){
			throw new RuntimeException("网络请求传入了单数个参数");
		}
		final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url, callBack,clazz, null, param, true);
		return request;
	}

	public <T> FFNetWorkRequest<T> upload(String url, String words,
			FFNetWorkCallBack<T> callBack,Class<T> clazz, File[] files, Object... param) {
		if(param != null && param.length%2==1){
			throw new RuntimeException("网络请求传入了单数个参数");
		}
		final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url, callBack,clazz, files, param, false);
		return request;
	}
	protected <T> void excuteHttp(FFNetWorkRequest<T> request)
			throws ClientProtocolException, SocketTimeoutException,
			ConnectException, UnsupportedEncodingException, IOException {
		FFLogUtil.e(TAG + "请求网址", request.getUrl());
		if (!FFUtils.checkNet()) {
			request.setStatus(FFResponseCode.ERROR_NATIVE_NET_CLOST, "网络未连");
			return;
		}
		long r_o = TrafficStats.getUidRxBytes(Process.myUid());
		long t_o = TrafficStats.getUidTxBytes(Process.myUid());
		long startTime = SystemClock.currentThreadTimeMillis();
		URL url = new URL(request.getUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(FFConfig.getNetTimeOut());
		conn.setReadTimeout(FFConfig.getNetTimeOut());
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("User-Agen", "android");
//		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		if (null == request.getParams() && null == request.getFiles()) {
			conn.setRequestMethod("GET");
		} else if (null != request.getFiles()) {
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			byte[] end_data = ("\r\n-----------7d4a6d158c9--\r\n").getBytes();// 定义最后数据分隔线
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=---------7d4a6d158c9");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			int fileLength = request.getFiles() == null ? 0 : request
					.getFiles().length;
			int paramsLength = request.getParams() == null ? 0 : request
					.getParams().length;
			int leng = fileLength + paramsLength;
			for (int i = 0; i < leng; i++) {
				if(i != 0){
					out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				}
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append("---------7d4a6d158c9");
				sb.append("\r\n");
				if (i < fileLength) {
					File file = request.getFiles()[i];
					sb.append("Content-Disposition: form-data;name=\"photo"
							+ "\";filename=\"" + file.getName() + "\"\r\n");
					sb.append("Content-Type:" + getFileType(file) + "\r\n\r\n");
					byte[] data = sb.toString().getBytes();
					out.write(data);
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				} else {
					sb.append("Content-Disposition: form-data;name=\""
							+ request.getParams()[i - fileLength] + "\"\r\n");
					sb.append("Content-Type:application/x-www-form-urlencoded; charset=utf-8\r\n\r\n");
					i++;
					if (request.getParams()[i - fileLength] != null) {
						sb.append(URLEncoder.encode(request.getParams()[i
								- fileLength].toString(), "utf-8"));
					}
					byte[] data = sb.toString().getBytes();
					out.write(data);
				}
			}
			out.write(end_data);
			out.flush();
			out.close();
		} else {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; charset=utf-8");
			conn.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			StringBuilder encodedParams = new StringBuilder();
			for (int i = 0; i < request.getParams().length; i++) {
				encodedParams.append(URLEncoder.encode(
						request.getParams()[i].toString(), "utf-8"));
				encodedParams.append('=');
				i++;
				encodedParams.append(URLEncoder.encode(
						request.getParams()[i] == null
								? ""
								: request.getParams()[i].toString(), "utf-8"));
				encodedParams.append('&');
			}
			FFLogUtil.e("请求参数：", encodedParams.toString());
			encodedParams.append("/r/n");
			out.write(encodedParams.toString().getBytes("utf-8"));
			
			out.flush();
			out.close();
		}
		int responseCode = conn.getResponseCode();
		if (responseCode == 200) {
			BasicHttpEntity entity = new BasicHttpEntity();
			InputStream inputStream;
			try {
				inputStream = conn.getInputStream();
			} catch (IOException ioe) {
				inputStream = conn.getErrorStream();
			}
			entity.setContent(inputStream);
			entity.setContentLength(conn.getContentLength());
			entity.setContentEncoding(conn.getContentEncoding());
			entity.setContentType(conn.getContentType());
			T responseString;
			String string;
			String contentEncoding = conn.getContentEncoding();
			if (contentEncoding != null && contentEncoding.equals("gzip")) {
				string = unGZip(entity.getContent());
			} else {
				// 没进行过压缩，直接使用
				string = EntityUtils.toString(entity);
			}
			if(FFConfig.DEBUG){
				FFLogUtil.e("网络请求返回数据:", string);
				long r_n = TrafficStats.getUidRxBytes(Process.myUid()) - r_o;
				long t_n = TrafficStats.getUidTxBytes(Process.myUid()) - t_o;
				long useTime = SystemClock.currentThreadTimeMillis() - startTime;
				String msg = "本次请求使用流量使用情况\n上传:"+getBytes(t_n)+"\n下载:"+getBytes(r_n)+"\n总计:"+getBytes(r_n+t_n)+"\n耗时:"+useTime+"毫秒";
				FFLogUtil.i(TAG, msg);
				FFApplication.showToast(null, msg);
			}
			try {
				responseString = JSON.parseObject(string, request.getClazz());
			} catch (Exception e) {
				FFLogUtil.e("服务器返回数据解析失败", e);
				request.setStatus(FFResponseCode.ERROR_ANALYSIS, "服务器返回数据解析失败");
				return;
			}
			if (responseString instanceof FFBaseBean
					&& !((FFBaseBean) responseString).judge()) {
				request.setStatus(FFResponseCode.ERROR_BY_SERVICE,
						((FFBaseBean) responseString).getErrorMessage());
				request.setEntity(responseString);
			} else {
				request.setStatus(FFResponseCode.SUCCESS, "请求成功：服务器");
				request.setEntity(responseString);
			}
		} else if (responseCode == 404) {
			request.setStatus(FFResponseCode.ERROR_NET_404, "状态码404");
		} else if (responseCode == 505) {
			request.setStatus(FFResponseCode.ERROR_SITE_505, "状态码505");
		} else {
			request.setStatus(FFResponseCode.ERROR_SITE_XXX, "状态码"
					+ responseCode);
		}
		return;
	}
	public void stopAll() {
		es1.shutdown();
	}
	private static String getFileType(File file) {
		String type = null;
		switch (FFNetWorkUtils.getImageType(file)) {
			case 255216 :
				type = "image/jpeg";
				break;
			case 7173 :
				type = "image/gif";
				break;
			case 6677 :
				type = "image/bmp";
				break;
			case 13780 :
				type = "image/png";
				break;
			default :
				type = "image/jpeg";
				break;
		}
		return type;
	}

	private static String getBytes(long byteSum){
		if(byteSum < 1024){
			return byteSum+"字节";
		}
		if(byteSum < 1024 *1024){
			return FFUtils.getSubFloat(byteSum/1024f)+"K";
		}
		if(byteSum < 1024 *1024*1024){
			return FFUtils.getSubFloat(byteSum/1024f/1024f)+"M";
		}
		return byteSum + "字节";
	}
	public static String unGZip(InputStream in) {
		ByteArrayBuffer bt= new ByteArrayBuffer(1024);
		try {
			GZIPInputStream pIn = new GZIPInputStream(in);
			int l;
	        byte[] tmp = new byte[1024];
	        while ((l=pIn.read(tmp))!=-1){
	            bt.append(tmp, 0, l);
	        }
	        return new String(bt.toByteArray(),"utf-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 当前对象
	 */
	public void onDestory() {
		stopAll();
	}

	/**
	 * 本实例所对应的Activity是否已经finished
	 * @return
	 */
	public boolean getDestroyed() {
		if(activity == null){
			return false;
		}
		return activity.getDestroyed();
	}

}
