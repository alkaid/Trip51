package com.alkaid.base.common;

import android.os.Environment;

public class Constants {
	
	/** SD卡路径 */
	public static final String PATH_SD=Environment.getExternalStorageDirectory().getAbsolutePath();
	/** 组织路径 *//*
	public static final String PATH_COM=PATH_SD+"/alkaid";
	*//** 应用路径 *//*
	public static final String PATH_APP=PATH_COM+"/app";
	*//** 资源路径 *//*
	public static final String PATH_RES=PATH_APP+"/res";*/
	
	
	/** bundle传递数据时的key */
	public static class bundleKey{
		/** 需要弹出的信息*/
		public static final String toastMsg="toastMsg";
		/** 通用业务异常 */
		public static final String exception="exception";
	}
	
	/** message.what */
	public static class msgWhat{
		/** toast*/
		public static final int toast=3000;
		/** 通用业务异常 */
		public static final int exception=-1000;
	}
	
	/** sharedPreference 相关key */
	/*public static class sharedPreference{
		*//** 配置项*//*
		public static class config{
			*//** 配置项的sharedPreference文件名*//*
			public static String name="config";
		}
	}*/
}
