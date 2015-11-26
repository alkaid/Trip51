package com.alkaid.trip51.pay.ali;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alkaid.base.common.LogUtil;

public class AlixPayment {
	private final static String TAG = "AlixPayment";
	private Handler mHandler;

	private static AlixPayment instance = null;
	private Context mContext;

	private static final int RQF_PAY = 1;

	private AlixPayment() {

	}

	public static AlixPayment getInstance(Context context) {
		if (instance == null) {
			instance = new AlixPayment();
			instance.mContext = context;
			instance.init();
		}
		instance.mContext=context;
		return instance;
	}

	private void init(){
		mHandler = new Handler(Looper.getMainLooper()) {
			public void handleMessage(Message msg) {
//			LogUtil.e("AliPay msg.what="+msg.what+"msg.obj==null?"+(msg.obj==null?"true":"false")+" msg.getData==null?"+(msg.getData()==null?"true":"false"));
				switch (msg.what) {
					case RQF_PAY:
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
	}

	/**
	 * 获取支付参数
	 */
	public void Analytical(String aliParams) {
		CallPayment(aliParams);
	}

	/***
	 * 调用支付SDK
	 */
	private void CallPayment(final String aliParams) {
		// 完整的符合支付宝参数规范的订单信息
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ getSignType();
		// 获取订单组装字符串
		new Thread() {
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) mContext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(aliParams);
				LogUtil.e("alipay=" + result);
				Message msg = mHandler.obtainMessage(RQF_PAY);
				msg.what = RQF_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}


	/**
	 * create the order info. 创建订单信息
	 *
	 */
//	public String getOrderInfo(String subject, String body, String price) {
//
//		// 签约合作者身份ID
//		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
//
//		// 签约卖家支付宝账号
//		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
//
//		// 商户网站唯一订单号
//		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
//
//		// 商品名称
//		orderInfo += "&subject=" + "\"" + subject + "\"";
//
//		// 商品详情
//		orderInfo += "&body=" + "\"" + body + "\"";
//
//		// 商品金额
//		orderInfo += "&total_fee=" + "\"" + price + "\"";
//
//		// 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//				+ "\"";
//
//		// 服务接口名称， 固定值
//		orderInfo += "&service=\"mobile.securitypay.pay\"";
//
//		// 支付类型， 固定值
//		orderInfo += "&payment_type=\"1\"";
//
//		// 参数编码， 固定值
//		orderInfo += "&_input_charset=\"utf-8\"";
//
//		// 设置未付款交易的超时时间
//		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//		// 取值范围：1m～15d。
//		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";
//
//		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";
//
//		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//		// orderInfo += "&paymethod=\"expressGateway\"";
//
//		return orderInfo;
//	}
}
