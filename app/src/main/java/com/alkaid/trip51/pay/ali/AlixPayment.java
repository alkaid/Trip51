package com.alkaid.trip51.pay.ali;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.model.response.ResPayInfo;
import com.alkaid.trip51.pay.Payment;
import com.alkaid.trip51.pay.PaymentCallback;
import com.alkaid.trip51.pay.Result;

import java.net.URLEncoder;

public class AlixPayment extends Payment{
	private final static String TAG = "AlixPayment";
	private Handler mHandler;

	private static AlixPayment instance = null;
	private Context mContext;

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
					case MSG_WHAT_PAY_COMPLETE:
						String orderNo=msg.getData().getString("orderNo");
						Result result=new Result(Result.RQF_ORDER_SDK_FAIL);
						result.setOrderno(orderNo);
						if(null==msg.obj){
							//TODO 失败
							onComplete(orderNo,result);
							return;
						}
						PayResult payResult = new PayResult((String) msg.obj);
						// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
						String resultInfo = payResult.getResult();
						String resultStatus = payResult.getResultStatus();

						// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
						if (TextUtils.equals(resultStatus, "9000")) {
//						Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
							result.setCode(Result.RQF_ORDER_SUCCESS);
						} else {
							// 判断resultStatus 为非“9000”则代表可能支付失败
							// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
							if (TextUtils.equals(resultStatus, "8000")) {
//							Toast.makeText(PayDemoActivity.this, "支付结果确认中",Toast.LENGTH_SHORT).show();
								result.setCode(Result.RQF_ORDER_SUCCESS);
							}else if(TextUtils.equals(resultStatus, "4000")||TextUtils.equals(resultStatus, "6001")||TextUtils.equals(resultStatus, "6002")){
//							PayUtils.handleUserCancelOrderWithChangeWay(mContext, p);
								//TODO 用户取消操作
								result.setCode(Result.RQF_PAY_USER_CANCEL);
								result.setError(payResult.getMemo());
							} else {
								result.setError(payResult.getMemo());
								// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//								Toast.makeText(mContext, "支付失败，请稍候重试", Toast.LENGTH_SHORT).show();
							}
						}
						onComplete(orderNo,result);
						break;
					default:
						break;
				}
			};
		};
	}

	@Override
	public void pay(final String orderNo, ResPayInfo.PayInfo params, PaymentCallback paymentCallback) {
		ResPayInfo.AliPayInfo p=null;
		if(params instanceof ResPayInfo.AliPayInfo){
			p=(ResPayInfo.AliPayInfo)params;
		}else{
			throw new RuntimeException("The pay params must be ResPayInfo.AliPayInfo");
		}
		super.pay(orderNo, p, paymentCallback);
		final String orderInfo=getOrderInfo(p);
		final String payInfo = orderInfo + "&sign=\"" + URLEncoder.encode(p.sign) + "\"&"
				+"sign_type=\"" +p.sign_type+"\"";
		LogUtil.v(payInfo);
		// 获取订单组装字符串
		new Thread() {
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) mContext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				LogUtil.v("alipay=" + result);
				Message msg = mHandler.obtainMessage(MSG_WHAT_PAY_COMPLETE);
				msg.what = MSG_WHAT_PAY_COMPLETE;
				msg.obj = result;
				msg.getData().putString("orderNo",orderNo);
				mHandler.sendMessage(msg);
			}
		}.start();
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

	}


	/**
	 * create the order info. 创建订单信息
	 *
	 */
	public String getOrderInfo(ResPayInfo.AliPayInfo p) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + p.partner + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + p.seller_id + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + p.outtradeno + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + p.subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + p.body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + p.total_fee + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + p.notifyurl + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\""+p.service+"\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\""+p.payment_type+"\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\""+p._input_charset+"\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\""+p.it_b_pay+"\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
}
