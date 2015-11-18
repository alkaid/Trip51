package com.alkaid.base.extern.baseView;

import com.alkaid.base.view.base.BApp;

/**
 * @author Alkaid
 *
 */

public class BaseApp extends BApp{

	/**
	 * 获得全局单例
	 *
	 * @return
	 */
	public static BaseApp instance() {
		return (BaseApp) instance;
	}

	
}
