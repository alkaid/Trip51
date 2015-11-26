package com.alkaid.trip51;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.alkaid.trip51.pay.Result;
import com.alkaid.trip51.pay.WXPayment;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	private String orderno;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new LinearLayout(this));
        
//    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api= WXPayment.getInstance().getApi();
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		if(req instanceof PayReq){
			orderno = ((PayReq)req).extData;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		Log.d(TAG, "errstr = " + resp.errStr);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
			Result result=new Result();
			result.setOrderno(orderno);

			switch (resp.errCode) {
			case 0:
				//支付成功
				result.setCode(Result.RQF_PAYMENT_SUCCESS);
				break;
			case -1:
				//支付失败
//				Toast.makeText(this, "支付失败，请稍后重试", Toast.LENGTH_LONG).show();
				result.setCode(Result.RQF_ORDER_SDK_FAIL);
				break;
			case -2:
				//取消支付
//				Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
				result.setCode(Result.RQF_PAY_USER_CANCEL);
				break;
			default:
				result.setCode(Result.RQF_ORDER_SDK_FAIL);
				break;
			}
			WXPayment.getInstance().sendMsg(result);
			this.finish();
		}
	}
}