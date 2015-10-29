package com.alkaid.base.common;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class HttpConfig {
	public static final String TAG="HttpRequest";
	/** 连接超时时间 */
	private static final int TIMEOUT_CONN = 5000;
	/** 请求超时时间 */
	private static final int TIMEOUT_SOCKET = 5000;
	/** 重试次数 */
	private static final int RETRY_COUNT=3;
	/** 异常处理类 */
	private static final RetryHandler DEFAULT_RETRY_HANDLER=new RetryHandler(RETRY_COUNT);
	
	private int timeoutConn=TIMEOUT_CONN;
	private int timeoutSocket=TIMEOUT_SOCKET;
	private int retryCount=RETRY_COUNT;
	private RetryHandler retryHandler=DEFAULT_RETRY_HANDLER;
	
	private static HttpConfig instance;
	private HttpConfig(){}
	public static HttpConfig getInstance(){
		if(null==instance)
			instance=new HttpConfig();
		return instance;
	}
	
	public void resetConfig(){
		this.timeoutConn=TIMEOUT_CONN;
		this.timeoutSocket=TIMEOUT_SOCKET;
		this.retryCount=RETRY_COUNT;
		this.retryHandler=DEFAULT_RETRY_HANDLER;
	}
	
	public void setConfig(int timeoutConn,int timeoutSocket,int retryCount){
		this.timeoutConn=timeoutConn;
		this.timeoutSocket=timeoutSocket;
		this.retryCount=retryCount;
		this.retryHandler=new RetryHandler(retryCount);
	}
	
	public static class RetryHandler implements HttpRequestRetryHandler{
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
            org.apache.http.HttpRequest request = (org.apache.http.HttpRequest) context.getAttribute( 
                    ExecutionContext.HTTP_REQUEST); 
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);  
            if (idempotent) { 
                // 请求内容相同则重试  
                return true; 
            } 
            return false; 
		}
	}

	public int getTimeoutConn() {
		return timeoutConn;
	}
	public void setTimeoutConn(int timeoutConn) {
		this.timeoutConn = timeoutConn;
	}
	public int getTimeoutSocket() {
		return timeoutSocket;
	}
	public void setTimeoutSocket(int timeoutSocket) {
		this.timeoutSocket = timeoutSocket;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public RetryHandler getRetryHandler() {
		return retryHandler;
	}
	public void setRetryHandler(RetryHandler retryHandler) {
		this.retryHandler = retryHandler;
	}
	
	
}
