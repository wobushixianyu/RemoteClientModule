package com.oray.sunlogin.util;

import android.content.Context;
import android.view.Surface;
import android.view.WindowManager;

import com.oray.sunlogin.application.SunloginApplication;


public class UIUtils {
    /**
     * 获取全局Context对象
     * @return
     */
    public static Context getContext(){
    	return SunloginApplication.getAppContext();
    }

	/**
	 * 获取当前屏幕旋转角度
	 *
	 * @param context
	 * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
	 */
	public static int getDisplayRotation(Context context) {
		if(context == null)
			return 0;

		int rotation = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getRotation();
		switch (rotation) {
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
		}
		return 0;
	}
}