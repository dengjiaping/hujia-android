package com.ihujia.hujia;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.common.utils.StringUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ihujia.hujia.launcher.service.InitializeService;
import com.ihujia.hujia.login.utils.UserCenter;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;


/**
 * Created by liuzhichao on 2016/12/12.
 * application
 */
public class MainApplication extends MultiDexApplication {

	public static final String CHANNEL_KEY = "UMENG_CHANNEL";

	@Override
	public void onCreate() {
		super.onCreate();
		InitializeService.start(this);
//		LeakCanary.install(this);
		//图片加载初始化
		Fresco.initialize(this);
		//需要在应用主进程中初始化的操作
		if (isMainProcess()) {
			initBugly();
		}
	}

	public boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = android.os.Process.myPid();
		for (ActivityManager.RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	public void initBugly() {
		CrashReport.initCrashReport(this);
		CrashReport.setUserId(UserCenter.getUserLoginName(this));
		CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
	}

	/**
	 * 获取application中指定的meta-data
	 * @return 如果没有获取成功(没有对应值或者异常)，则返回值为空
	 */
	public static String getAppMetaData(Context ctx, String key) {
		String resultData = "android";
		if (ctx == null || StringUtil.isEmpty(key)) {
			return resultData;
		}
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key) == null ? "android" : applicationInfo.metaData.getString(key);
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return resultData;
	}
}
