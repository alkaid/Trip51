package com.alkaid.trip51.pay;

import java.io.Serializable;
/**
 * 登录结果或支付结果
 *
 */
public class Result implements Serializable{
	/** 登录成功*/
	public static final int RQF_LOGGEDIN_SUCCESS=1;
	/** 订单提交成功*/
	public static final int RQF_ORDER_SUCCESS=2;
	/** 订单支付成功*/
	public static final int RQF_PAYMENT_SUCCESS=3;
	/** 获取支付信息成功*/
	public static final int RQF_GET_PAYMENT_INFO_SUCCESS=4;
	/** 支付初始化成功*/
	public static final int RQF_PAYINIT_SUCCESS=5;
	/** 行为日志记录成功*/
	public static final int RQF_RECORD_ACTION_SUCCESS=6;
	/** 礼包内容获取成功*/
	public static final int RQF_GET_GIFT_CONTENT_SUCCESS=51;
	//以下是登录代码
	/** 无需第三方登录*/
	public static final int RQF_LOGGEDIN_NO_THIRDLOGIN=100;
	/** 第三方登录成功且已绑定token*/
	public static final int RQF_LOGGEDIN_THIRDLOGIN_SUCCESS=101;
	/** 第三方登录失败*/
	public static final int RQF_LOGGEDIN_THIRDLOGIN_FAIL=102;
	/** token绑定失败*/
	public static final int RQF_LOGGEDIN_BIND_TOKEN_FAIL=103;
	/** 登出成功*/
	public static final int RQF_LOGOUT_SUCCESS=104;
	/** 登出失败*/
	public static final int RQF_LOGOUT_FAIL=105;
	
	//以下是支付代码
	/** 用户未登录*/
	public static final int RQF_NOT_LOGGEDIN=1001;
	/** 网络异常*/
	public static final int RQF_NET_ERROR=1002;
	/** 获取设备信息异常,请检查sim卡 */
	public static final int RQF_DEVICE_INFO_ERROR=1003;
	/** 服务端返回错误信息*/
	public static final int RQF_SERVER_NOTICE_ERROR=1004;
	/** 用户取消支付*/
//	public static final int RQF_USER_CANCEL=1005;
	/** 短信发送失败*/
	public static final int RQF_SMS_SEND_ERROR=1005;
	/** 服务端返回数据有误*/
	public static final int RQF_SERVER_RESPONSE_ERROR=1006;
	/** 支付失败：支付初始化未完成，需稍后重试*/
	public static final int RQF_PAY_FAIL_PAYINITING=1007;
	/** 支付失败：支付列表数据为空，正在重新进行初始化，需稍后重试*/
	public static final int RQF_PAY_FAIL_REPAYINIT=1008;
	/** 参数格式错误*/
	public static final int RQF_ILLEGAL_ARGUMENT=1009;
	/** 用户取消支付*/
	public static final int RQF_PAY_USER_CANCEL=1010;
	/** 调用SDK支付失败*/
	public static final int RQF_ORDER_SDK_FAIL=1012;
	/** 订单超时*/
	public static final int RQF_ORDER_OVER_TIME=1013;
	
	
	private int code;
	private String error;
//	private boolean confirm=true;//是否可显示默认弹框
	private String orderno;
//	private String paymentInfo;
	/**附加数据 多用于SDK返回的信息*/
	private String external;
	
	public Result(){}
	public Result(int code){
		this.setCode(code);
	}
	public Result(int code,String external){
		this.external=external;
		this.setCode(code);
	}
	public String getOrderno() {
		return orderno;
	}

	void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
//	/** 获取标识:是否显示弹框 (注意此参数仅在登录接口{@link Mfsdk#login()}中返回)*/
//	public boolean isConfirm() {
//		return confirm;
//	}
//	void setConfirm(boolean confirm) {
//		this.confirm = confirm;
//	}
	
	/** 获取结果状态码*/
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		switch (code) {
		case RQF_LOGGEDIN_SUCCESS:
			error="登录成功";
			break;
		case RQF_ORDER_SUCCESS:
			error="订单提交成功";
			break;
		case RQF_PAYMENT_SUCCESS:
			error="订单支付成功";
			break;
		case RQF_GET_PAYMENT_INFO_SUCCESS:
			error="获取支付信息成功";
			break;
		case RQF_RECORD_ACTION_SUCCESS:
			error="行为日志记录成功";
			break;
		case RQF_NOT_LOGGEDIN:
			error="用户未登录";
			break;
		case RQF_NET_ERROR:
			error="网络异常";
			break;
		case RQF_DEVICE_INFO_ERROR:
			error="获取设备信息异常,请检查sim卡";
			break;
		case RQF_SERVER_NOTICE_ERROR:
			error="服务端返回错误信息";
			break;
//		case RQF_USER_CANCEL:
//			error="用户取消支付";
//			break;
		case RQF_SMS_SEND_ERROR:
			error="短信发送失败";//检查是否被拦截
			break;
		case RQF_SERVER_RESPONSE_ERROR:
			error="服务端返回的数据有误";
			break;
		case RQF_PAY_FAIL_PAYINITING:
			error="支付失败：支付初始化未完成，需稍后重试";
			break;
		case RQF_PAY_FAIL_REPAYINIT:
			error="支付失败：支付列表数据为空，正在重新进行初始化，需稍后重试";
			break;
		case RQF_ILLEGAL_ARGUMENT:
			error="参数格式错误";
			break;
		case RQF_PAY_USER_CANCEL:
			error="用户取消支付";
			break;
		case RQF_ORDER_SDK_FAIL:
			error="调用SDK支付失败";
			break;
		case RQF_LOGGEDIN_THIRDLOGIN_SUCCESS:
			error="第三方登录成功且已绑定token";
			break;
		case RQF_LOGGEDIN_NO_THIRDLOGIN:
			error="无需第三方登录";
			break;
		case RQF_LOGGEDIN_THIRDLOGIN_FAIL:
			error="第三方登录失败";
			break;
		case RQF_LOGGEDIN_BIND_TOKEN_FAIL:
			error="token绑定失败";
			break;
		case RQF_LOGOUT_SUCCESS:
			error="登出成功";
			break;
		case RQF_LOGOUT_FAIL:
			error="登出失败";
			break;
		default:
			error="";
			break;
		}
	}
	public void setError(String error) {
		this.error = error;
	}
	/** 获取错误提示信息*/
	public String getError() {
		return error;
	}
	/**附加数据 多用于SDK返回的信息*/
	public String getExternal() {
		return external;
	}
	/**附加数据 多用于SDK返回的信息*/
	public void setExternal(String external) {
		this.external = external;
	}

//	public String getPaymentInfo() {
//		return paymentInfo;
//	}
//
//	void setPaymentInfo(String paymentInfo) {
//		this.paymentInfo = paymentInfo;
//	}
	
}
