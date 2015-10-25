package com.alkaid.base.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

/**
 * 通用Http联网类 <br/>
 * 默认配置如下：<br/>
 * 		连接超时：3000ms  请求超时:5000ms 重试次数：3次<br/>
 * 用完后必须调用 {@link #destroy()}方法来销毁对象<br/>
 * 要重用实例必须调用 {@link #reset(boolean)}方法重置
 * 
 * @author alkaid
 *
 */
public class HttpRequest {
	//----------在普通java工程中放开以下两个内部类的注释 Android工程中不用------------------
	/*private static class TextUtils{
		public static boolean isEmpty(String str){
			return null==str||"".equals(str)||str.trim().equals("");
		}
	}
	
	private static class LogUtil{
		private static void i(String msg){
			System.out.println(msg);
		}
		private static void e(String tag,String msg,Exception e){
			new RuntimeException(tag+": "+msg,e).printStackTrace();
		}
	}*/
	public static final String TAG="HttpRequest";
	
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String HEADER_SET_COOKIE = "set-cookie";
	public static final String HEADER_COOKIE = "Cookie";
	public static final String HEADER_HOST = "Host";
	public static final String HEADER_CONNECTION = "Connection";
	public static final String HEADER_REFERER = "Referer";
	public static final String HEADER_CACHE_CONTROL = "Cache-Control";
	public static final String HEADER_ORIGIN = "Origin";
	public static final String HEADER_USER_AGENT = "User-Agent";
	/** 连接超时时间 */
	private static final int TIMEOUT_CONN = 5000;
	/** 请求超时时间 */
	private static final int TIMEOUT_SOCKET = 5000;
	/** 重试次数 */
	private static final int RETRY_COUNT=3;
	/** 异常处理类 */
	private static final RetryHandler DEFAULT_RETRY_HANDLER=new RetryHandler(RETRY_COUNT);
	
	//入参
	private String url;
	private String method=METHOD_GET;
	private Map<String, String> params;
	private Map<String, String> header;
	private String entityStr;
	private CookieStore cookieStore;
	private String charset="utf-8";
//	private boolean useSingleClient; //是否使用单例HttpClient
	//出参
	private DefaultHttpClient client;
	private HttpResponse response;
	private HttpEntity responseEntity;
	/**
	 * HttpClient默认使用单例模式<br/>
	 * 每次实例化都重置配置项为默认配置<br/>
	 * 默认配置为:<br/>
	 * 连接超时：3000ms  请求超时:5000ms 重试次数：3次
	 */
	public HttpRequest(){
		client= HttpClientHolder.getHttpClient();
		setDefaultConfig();
	}
	/**
	 * 每次实例化都重置配置项为默认配置<br/>
	 * 默认配置为:<br/>
	 * 连接超时：3000ms  请求超时:5000ms 重试次数：3次
	 * @param useSingleClient 是否使用单例
	 */
	public HttpRequest(boolean useSingleClient){
//		this.useSingleClient=useSingleClient;
		client= useSingleClient?HttpClientHolder.getHttpClient():new DefaultHttpClient();
		setDefaultConfig();
	}
	/**
	 * 重置<br/>  包括清除response、重新获得HttpClient 和 重置配置项
	 * @param resetConfig 是否重置配置项
	 */
	public HttpRequest reset(boolean resetConfig){
		destroy();
//		client= useSingleClient?HttpClientHolder.getHttpClient():new DefaultHttpClient();
		if(resetConfig){
			setDefaultConfig();
		}
		return this;
	}
	/** 销毁对象 */
	public void destroy(){
//		try {
//			responseEntity.getContent().close();
//		} catch (IllegalStateException e) {
//			LogUtil.e(TAG,"",e);
//		} catch (IOException e) {
//			LogUtil.e(TAG,"",e);
//		}
		try {
			responseEntity.consumeContent();
		} catch (IOException e) {
			LogUtil.e(TAG,"",e);
		}
//		client.getConnectionManager().shutdown();
		params=null;
		responseEntity=null;
		response=null;
	}
	/** 恢复默认配置 <br/>
	 * 连接超时：3000ms  请求超时:5000ms 重试次数：3次*/
	public HttpRequest setDefaultConfig(){
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_CONN);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				TIMEOUT_SOCKET);
		//默认异常处理
		client.setHttpRequestRetryHandler(DEFAULT_RETRY_HANDLER);
		//默认不管cookie HttpClient里保存的cookie是什么就是什么
//		cookieStore=client.getCookieStore();
//		client.setCookieStore(cookieStore);
		return this;
	}
	/** 设置连接超时时间 */
	public HttpRequest setConnectionTimeout(int time){
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, time);
		return this;
	}
	/** 设置请求超时时间 */
	public HttpRequest setSocketTimeout(int time){
		client.getParams().setParameter(
				CoreConnectionPNames.SO_TIMEOUT, time);
		return this;
	}
	/** 设置重试次数 */
	public HttpRequest setRetryCount(int count){
		client.setHttpRequestRetryHandler(new RetryHandler(count));
		return this;
	}
	/** 设置请求重试异常处理 */
	public HttpRequest setRequestRetryHandler(HttpRequestRetryHandler handler){
		client.setHttpRequestRetryHandler(handler);
		return this;
	}
	/** 设置请求地址*/
	public HttpRequest setUrl(String url) {
		this.url = url;
		return this;
	}
	/** 设置请求方法,值为   {@link #METHOD_GET} 或 {@link #METHOD_POST}*/
	public HttpRequest setMethod(String method) {
		this.method = method;
		return this;
	}
	/** 设置get/post参数 */
	public HttpRequest setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}
	/** 设置请求头*/
	public HttpRequest setHeader(Map<String, String> header) {
		this.header = header;
		return this;
	}
	/** 设置get/post参数 为字符串形式*/
	public HttpRequest setEntityStr(String entityStr) {
		this.entityStr = entityStr;
		return this;
	}
	/** 设置cookie*/
	public HttpRequest setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}
	/** 设置响应内容的编码格式*/
	public HttpRequest setCharset(String charset) {
		this.charset = charset;
		return this;
	}
	/** 获得response*/
	public HttpResponse getResponse() {
		return response;
	}
	/** 获得httpClient*/
	public DefaultHttpClient getClient() {
		return client;
	}
	/** 获得响应结果*/
	public HttpEntity getResponseEntity() {
		return responseEntity;
	}
	/** 获得cookie*/
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	/**
	 * 获得响应结果
	 * @return
	 * @throws IOException
	 */
	public byte[] getResponseByte() throws IOException{
		throwNonExecException();
		return EntityUtils.toByteArray(responseEntity);
	}
	/**
	 * 获得响应结果
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public InputStream getResponseInputStream() throws IllegalStateException, IOException{
		throwNonExecException();
		return responseEntity.getContent();
	}
	/**
	 * 获得响应结果
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String getResponseString() throws ParseException, IOException{
		throwNonExecException();
		return EntityUtils.toString(responseEntity, charset);
	}
	//为了检验HttpRequest是否执行了exec()方法 抛出一个RuntimeExcetpion
	private void throwNonExecException(){
		if(null==responseEntity){
			throw new NullPointerException("You must call the method exec() before!The response is null!");
		}
	}
	/**
	 * 提交请求  为response和entity赋值
	 * @return
	 * @throws IOException
	 */
	public HttpRequest exec() throws IOException {
		HttpUriRequest req = null;
		// url添加get参数
		if (method.equals(METHOD_GET)) {
			//form键值对参数方式
			if(null!=params && params.size()>0){
				url=getUrlAppendParams(url, params,charset);
			//entity String参数方式
			}else if(!TextUtils.isEmpty(this.entityStr)){
				if(url.contains("?")){
					url = url + "&"+entityStr;
				}else{
					url = url + "?" + entityStr;
				}
			}
			req = new HttpGet(url);
		} else {
			req = new HttpPost(url);
			//form键值对参数方式
			if(null!=params && params.size()>0){
				List<NameValuePair> postParams = new ArrayList<NameValuePair>();
				for (String key : params.keySet()) {
					postParams.add(new BasicNameValuePair(key, params.get(key)));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams,charset);
				((HttpPost) req).setEntity(entity);
			//entity String参数方式
			}else if(!TextUtils.isEmpty(this.entityStr)){
				StringEntity strEntity=new StringEntity(entityStr);
				((HttpPost) req).setEntity(strEntity);
			}
		}
		//设置cookie
		if(null != cookieStore){
			client.setCookieStore(cookieStore);
		}
		// 设置header
		if (null != header && header.size() > 0) {
			for (String key : header.keySet()) {
				req.setHeader(key, header.get(key));
			}
		}
		LogUtil.i("---url---"+url);
		this.response= client.execute(req);
		this.responseEntity=response.getEntity();
		this.cookieStore=client.getCookieStore();
		return this;
	}

	static class RetryHandler implements HttpRequestRetryHandler{
		private int retryCount;
		public RetryHandler(int retryCount){
			this.retryCount=retryCount;
		}
		@Override
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			if(executionCount > retryCount){
				return false;
			}
			if(exception instanceof ConnectTimeoutException){
				LogUtil.e(TAG, "http连接超时,共执行了"+executionCount+"次", exception);
				return true;
			}
			if(exception instanceof SocketTimeoutException){
				LogUtil.e(TAG, "http请求超时,共执行了"+executionCount+"次", exception);
				return true;
			}
			if (exception instanceof NoHttpResponseException) { 
                // 服务停掉则重新尝试连接 
                return true; 
            } 
            if (exception instanceof SSLHandshakeException) { 
                // SSL异常不需要重试 
                return false; 
            } 
            if (exception instanceof UnknownHostException){
            	LogUtil.e(TAG, "http找不到主机,共执行了"+executionCount+"次", exception);
            	return true;
            }
            if (exception instanceof NoHttpResponseException) {
                // Retry if the server dropped connection on us
                return true;
            }
//            if (exception instanceof InterruptedIOException) {
//                // Timeout
//                return false;
//            }
            HttpRequest request = (HttpRequest) context.getAttribute( 
                    ExecutionContext.HTTP_REQUEST); 
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);  
            if (idempotent) { 
                // 请求内容相同则重试  
                return true; 
            } 
            return false; 
		}
	}
	/**
	 * 将指定的参数转换成Url参数的方式 形如 id=001&user=0002
	 * @param url
	 * @param params
	 *            url参数 或 entity参数
	 * @param charset 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getUrlAppendParams(String url,Map<String, String> params,String charset) throws UnsupportedEncodingException {
		if(TextUtils.isEmpty(charset))
			charset="utf-8";
		StringBuilder strb = new StringBuilder(url);
		if (null != params && params.size() > 0) {
			boolean isFirstParam = !url.contains("?");
			if(isFirstParam)
				strb.append("?");
			for (String key : params.keySet()) {
				if (isFirstParam)
					isFirstParam = false;
				else
					strb.append("&");
				strb.append(key).append("=").append(URLEncoder.encode(params.get(key),charset));
			}
		}
		return strb.toString();
	}

}
