package com.alkaid.trip51.pay;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alkaid.trip51.model.response.ResPayInfo;
import com.alkaid.trip51.pay.ali.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayment extends Payment<ResPayInfo.WechatPayInfo>{
	private final static String TAG = "WXPayment";
	private static WXPayment instance = null;
	private Handler mHandler;
	private Context mContext;
	private IWXAPI api;
	private WXPayment() {
	}

	public static WXPayment getInstance(Context context) {
		if (instance == null) {
			instance = new WXPayment();
			instance.mContext = context;
			instance.init();
		}
		instance.mContext=context;
		return instance;
	}
	public static WXPayment getInstance(){
		if(instance==null){
			throw new RuntimeException("instance can't be null!must call getInstance(Context context) first!");
		}
		return instance;
	}

	private void init(){
		mHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message msg) {
//			LogUtil.e("AliPay msg.what="+msg.what+"msg.obj==null?"+(msg.obj==null?"true":"false")+" msg.getData==null?"+(msg.getData()==null?"true":"false"));
				switch (msg.what) {
					case MSG_WHAT_PAY_COMPLETE:
						if(null==msg.obj){
							//TODO 失败
							return;
						}
						PayResult payResult = new PayResult((String) msg.obj);
						// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
						String resultInfo = payResult.getResult();
						String resultStatus = payResult.getResultStatus();

						// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
						if (TextUtils.equals(resultStatus, "9000")) {
//						Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
							//TODO 查询web支付结果
						} else {
							// 判断resultStatus 为非“9000”则代表可能支付失败
							// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
							if (TextUtils.equals(resultStatus, "8000")) {
//							Toast.makeText(PayDemoActivity.this, "支付结果确认中",Toast.LENGTH_SHORT).show();
								//TODO 查询web支付结果
							}else if(TextUtils.equals(resultStatus, "4000")||TextUtils.equals(resultStatus, "6001")||TextUtils.equals(resultStatus, "6002")){
//							PayUtils.handleUserCancelOrderWithChangeWay(mContext, p);
								//TODO 用户取消操作
							} else {
								// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
								Toast.makeText(mContext, "支付失败，请稍候重试", Toast.LENGTH_SHORT).show();
							}
						}
						break;
					default:
						break;
				}
			};
		};
	}

	/**
	 * 初始化支付
	 */
	public void initPayment() {
		// api = WXAPIFactory.createWXAPI(mContext, APP_ID);
		// api.registerApp(APP_ID);
	}

	@Override
	public void pay(String orderNo, ResPayInfo.WechatPayInfo p, PaymentCallback paymentCallback) {
		super.pay(orderNo, p, paymentCallback);
		api = WXAPIFactory.createWXAPI(mContext, p.appid);
		api.registerApp(p.appid);

		PayReq req = new PayReq();
		req.appId = p.appid;// 应用ID
		req.partnerId = p.partnerid;// 商户id
		req.prepayId = p.prepayid;// 預支付訂單
		req.nonceStr = p.noncestr;// 隨機串
		req.timeStamp = p.timestamp;// 時間戳
		req.packageValue = p._package;
//		req.packageValue = "Sign=" + packageValue;// 商家根据文档填写的数据和签名
		// req.packageValue = "Sign=WXPay";// 商家根据文档填写的数据和签名
		req.sign=p.sign;

		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		if (!api.isWXAppInstalled()) {
			Log.e(TAG, "can't find weixin app");
			Toast.makeText(mContext, "未安装微信，支付失败",Toast.LENGTH_LONG).show();
		} else {
			api.sendReq(req);
		}
	}

	public IWXAPI getApi() {
		return api;
	}

	public void sendMsg(Result result){
		Message msg= mHandler.obtainMessage(MSG_WHAT_PAY_COMPLETE);
		msg.obj=result;
		mHandler.sendMessage(msg);
	}
}
