package com.alkaid.trip51.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.alkaid.base.common.SystemUtil;
import com.alkaid.trip51.base.widget.App;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * Created by df on 2015/12/10.
 */
public class UpdateUtil {
    public static void update(){
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        String value = OnlineConfigAgent.getInstance().getConfigParams(App.instance(), "force_upgrade_version_list");
        if(TextUtils.isEmpty(value)){
            return;
        }
        String[] upgrade_mode_array = value.split(";");
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(App.instance());
        UmengUpdateAgent.forceUpdate(App.instance());//这行如果是强制更新就一定加上
        for(String mode:upgrade_mode_array){
            mode=mode.trim();
            String versionName = SystemUtil.getSoftVersion(App.instance());
            versionName = "v"+versionName;
            if(mode.equals(versionName)){
                //进入强制更新
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {

                    }
                });
                UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
                    @Override
                    public void onClick(int status) {
                        switch (status) {
                            case UpdateStatus.Update:
                                break;
                            default:
                                //退出应用
                                Toast.makeText(App.instance(),"抱歉，需要更新到版本才能正常使用",Toast.LENGTH_LONG).show();
                                App.instance().exit();
                        }
                    }
                });
                break;
            }
        }
    }
}
