package com.alkaid.trip51.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.common.SystemUtil;
import com.alkaid.trip51.base.widget.App;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by df on 2015/12/10.
 * 更新辅助类，变相实现了强制更新
 */
public class UpdateUtil {
    private static boolean needForceExit=false;
    private static boolean isDownloading=false;
    private static ProgressDialog pd=null;
    //先根据"force_upgrade_mode"的值来判断使用哪种规则来进行升级。
    // 1.若为0则关闭强制升级，采用默认升级
    // 2.若为force_upgrade_version_lower则按force_upgrade_version_lower的值若当前版本小于等于该值则进行强制升级。
    // 3.若为force_upgrade_version_list则按照force_upgrade_version_list枚举的版本列表，若当前版本在列表中，则进行强制升级
    public static void update(Context context){
        needForceExit=false;
        isDownloading=false;
        pd=null;
        String force_upgrade_mode_value= OnlineConfigAgent.getInstance().getConfigParams(context, "force_upgrade_mode");
        boolean forceUpdate=false;
        if("force_upgrade_version_list".equals(force_upgrade_mode_value)) {
            String force_upgrade_version_list_value = OnlineConfigAgent.getInstance().getConfigParams(context, "force_upgrade_version_list");
            if (!TextUtils.isEmpty(force_upgrade_version_list_value)) {
                String[] force_upgrade_array = force_upgrade_version_list_value.split(";");
                for (String version : force_upgrade_array) {
                    version = version.trim();
                    String versionName = SystemUtil.getSoftVersion(context);
                    versionName = "v" + versionName;
                    if (version.equals(versionName)) {
                        forceUpdate = true;
                        break;
                    }
                }
            }
        }else if("force_upgrade_version_lower".equals(force_upgrade_mode_value)){
            String force_upgrade_version_lower_value=OnlineConfigAgent.getInstance().getConfigParams(context, "force_upgrade_version_lower");
            if (!TextUtils.isEmpty(force_upgrade_version_lower_value)) {
                int target=0;
                try {
                    target=Integer.parseInt(force_upgrade_version_lower_value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if(target>0) {
                    int versionCode = SystemUtil.getVersionCode(context);
                    if (versionCode > 0 && versionCode<=target){
                        forceUpdate=true;
                    }
                }
            }
        }
        if(forceUpdate){
            forceUpdate(context);
        }else{
            defaultUpdate();
        }
    }
    //默认更新策略
    private static void defaultUpdate(){
        LogUtil.v("UMeng default update...");
        //wifi静默下载
//        UmengUpdateAgent.silentUpdate(context);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(App.instance());
        UmengUpdateAgent.forceUpdate(App.instance());//这行如果是强制更新就一定加上
    }
    private static void forceUpdate(final Context context){
        LogUtil.v("UMeng force update...");
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(App.instance());
        UmengUpdateAgent.forceUpdate(App.instance());//这行如果是强制更新就一定加上
        //进入强制更新
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {

            }
        });
        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
            @Override
            public void onClick(int status) {
                needForceExit=true;
                switch (status) {
                    case UpdateStatus.Update:
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(isDownloading){
                                    //如果已经开始下载 则不进行处理 交给下载完成来exit
                                }else{
                                    checkForceUpdateThenExit(context);
                                }
                            }
                        }, 1000);
                        break;
                    default:
                        //退出应用
                        checkForceUpdateThenExit(context);
                }
            }
        });
        UmengUpdateAgent.setDownloadListener(new UmengDownloadListener() {

            @Override
            public void OnDownloadStart() {
                isDownloading=true;
//                Toast.makeText(context, "download start", Toast.LENGTH_SHORT).show();
                pd= new ProgressDialog(context);
                pd.setMessage("正在下载:0%");
                pd.show();
            }

            @Override
            public void OnDownloadUpdate(int progress) {
//                Toast.makeText(context, "download progress : " + progress + "%", Toast.LENGTH_SHORT).show();
                pd.setMessage("正在下载:" + progress + "%");
//                pd.show();
            }

            @Override
            public void OnDownloadEnd(int result, String file) {
                //Toast.makeText(mContext, "download result : " + result , Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "download file path : " + file, Toast.LENGTH_SHORT).show();
                pd.dismiss();
//                checkForceUpdateThenExit(context);
                new Timer("aa").schedule(new TimerTask() {
                    @Override
                    public void run() {
                        App.instance().exit();
                    }
                },3000);
            }
        });
    }
    private static void checkForceUpdateThenExit(Context context){
        if(needForceExit) {
            AlertDialog.Builder b=new AlertDialog.Builder(context);
            b.setTitle("提示").setMessage("抱歉，由于有重要更新内容，需要更新版本才能正常使用").setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.instance().exit();
                        }
                    });
            b.show();
        }
    }
}
