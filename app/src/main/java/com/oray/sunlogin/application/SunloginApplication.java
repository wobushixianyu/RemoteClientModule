package com.oray.sunlogin.application;

import android.app.Application;
import android.content.Context;


public class SunloginApplication extends Application{

	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

	public static Context getAppContext(){
		return mContext;
	}
}
