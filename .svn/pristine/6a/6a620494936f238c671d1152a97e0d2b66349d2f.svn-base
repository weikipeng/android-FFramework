package com.fan.framework.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.fan.framework.config.FFConfig;

public class FFNetWorkUtils {
	/**
	 * 获取get方法字符
	 * @param paramArray
	 * @return
	 */
	public static String getGetString(Object... paramArray){
		StringBuilder params = new StringBuilder("?");
		if (paramArray != null && paramArray.length != 0) {
			int max = paramArray.length;
			for (int i = 0; i < max; i++) {
				Object str = paramArray[++i] == null ? "" : paramArray[i];
				try {
					params.append(paramArray[i - 1] + "="
							+ URLEncoder.encode(str.toString(), "utf-8") + "&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params.toString().substring(0, params.length() - 1);
	}

	/**
	 * 初始化httpClient
	 * @return 
	 */
	public static DefaultHttpClient getClient() {
			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(httpParams,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(httpParams, true);
			HttpConnectionParams.setConnectionTimeout(httpParams,
					FFConfig.NetConfig.CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams,
					FFConfig.NetConfig.CONNECTION_TIMEOUT);
			httpParams.setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			httpParams.setParameter("Transfer-Encoding", "chunked");
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			// 关键代码--事关线程安全�?
			ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(
					httpParams, schemeRegistry);
			return new DefaultHttpClient(clientConnectionManager, httpParams);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static int getImageType(File file) {
		if (!file.exists()) {
			return 0;
		}
		InputStream is = null;
		int fileType = 0;
		try {
			is = new FileInputStream(file);
			byte[] buffer = new byte[2];
			String fileCode = "";
			if (is.read(buffer) != -1) {
				for (int i = 0; i < buffer.length; i++) {
					fileCode += Integer.toString((buffer[i] & 0xFF));
				}
				fileType = Integer.parseInt(fileCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return fileType;
	}
}
