package com.alkaid.trip51.pay;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alkaid.trip51.model.response.ResPayInfo;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayment extends Payment{
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
						Result result= (Result) msg.obj;
						onComplete(result.getOrderno(),result);
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
	public void pay(String orderNo, ResPayInfo.PayInfo params, PaymentCallback paymentCallback) {
		ResPayInfo.WechatPayInfo p=null;
		if(params instanceof ResPayInfo.WechatPayInfo){
			p=(ResPayInfo.WechatPayInfo)params;
		}else{
			throw new RuntimeException("The pay params must be ResPayInfo.WechatPayInfo");
		}
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
		req.extData=orderNo;

		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		if (!api.isWXAppInstalled()) {
			Log.e(TAG, "can't find weixin app");
			Toast.makeText(mContext, "未安装微信，支付失败",Toast.LENGTH_LONG).show();
			Result result= new Result(Result.RQF_ORDER_SDK_FAIL);
			result.setOrderno(orderNo);
			sendMsg(result);
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
