package com.ihujia.hujia.launcher.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.common.utils.VersionInfoUtils;
import com.ihujia.hujia.R;
import com.ihujia.hujia.base.BaseActivity;
import com.ihujia.hujia.base.WebViewActivity;
import com.ihujia.hujia.db.CityDBManager;
import com.ihujia.hujia.home.controller.ProductDetailActivity;
import com.ihujia.hujia.main.controller.MainActivity;
import com.ihujia.hujia.otherstore.controller.StoreRenovationActivity;
import com.ihujia.hujia.person.controller.LogisticsDetailActivity;
import com.ihujia.hujia.person.controller.MessageActivity;
import com.ihujia.hujia.utils.ConfigUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuzhichao on 2016/12/16.
 * 启动页
 */
public class LauncherActivity extends BaseActivity {

    private static final long LAUNCH_MIN_TIME = 2000L;
    private static final String SIGNATURE = "95:fe:86:08:0a:17:f6:b5:95:43:f5:fb:35:02:9c:0e";
    private static final int MSG_CITY_INIT_FINISH = 1;
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_WEB_TITLE = "extra_web_title";
    public static final int PUSH_NOTICE = 1;//系统通知
    public static final int PUSH_PRODUCT = 2;//商品详情
    public static final int PUSH_STORE = 3;//店铺详情
    public static final int PUSH_WEBVIEW = 4;//web
    public static final int PUSH_LOGISTIC = 5;//物流

    private long mLaunchTime;
    private int pushFrom;
    private String content;
    private int versionCode = 1;

    private boolean canBack;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CITY_INIT_FINISH:
                    gotoActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launcher);
        pushFrom = getIntent().getIntExtra(EXTRA_TAG, 0);
        content = getIntent().getStringExtra(EXTRA_CONTENT);
        /*if (!LogUtil.isDebug && !SIGNATURE.equals(DeviceUtil.getAppMD5Signature(this))) {
			canBack = true;
			ToastUtil.longShow(this, "您的版本非官方发布版本，请选择正规商店下载");
			return;
		}*/
        mLaunchTime = SystemClock.elapsedRealtime();
        initCityDB();
    }

    private void initCityDB() {
        new Thread(() -> {
            CityDBManager.introducedCityDB(this);
            handler.sendEmptyMessageDelayed(MSG_CITY_INIT_FINISH, 100);
        }).start();
    }

    private void gotoActivity() {
        long elapsed = SystemClock.elapsedRealtime() - mLaunchTime;
        if (elapsed >= LAUNCH_MIN_TIME) {
            performGotoActivity();
            finish();
        } else {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (LauncherActivity.this.isFinishing()) {
                        return;
                    }
                    cancel();
                    performGotoActivity();
                    finish();
                }
            }, LAUNCH_MIN_TIME - elapsed);
        }
    }

    private void gotoMainActivity() {
        if (isNewVersion()) {
//			if (StringUtil.isEmpty(DeviceUtil.getDeviceIdData(this))) {
//				DeviceUtil.setDeviceIdData(this);
//			}

            ConfigUtils.setOldVersionCode(this, versionCode);
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        } else {
            if (ConfigUtils.isShowGuide(this)) {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            } else {
                switch (pushFrom) {
                    default: {
                        Intent intentMain = new Intent(LauncherActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        break;
                    }
                }
            }
        }
    }

    private void performGotoActivity() {
		/*if (CheckRootUtil.isDeviceRooted() && DeviceUtil.isEmulator(LauncherActivity.this)) {
			LauncherActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.shortShow(LauncherActivity.this, "暂不支持虚拟机登陆，请使用移动设备登陆！");
				}
			});
			finish();
		} else */
        if (isNewVersion()) {
			/*if (StringUtil.isEmpty(DeviceUtil.getDeviceIdData(this))) {
				DeviceUtil.setDeviceIdData(this);
			}*/

            ConfigUtils.setOldVersionCode(this, versionCode);
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        } else {
            if (ConfigUtils.isShowGuide(this)) {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                switch (pushFrom) {
                    case PUSH_NOTICE:
                        startActivity(new Intent(this, MessageActivity.class));
                        break;
                    case PUSH_PRODUCT:
                        ProductDetailActivity.startProductDetailActivity(this, content);
                        break;
                    case PUSH_STORE:
                        StoreRenovationActivity.startStoreRenovationActivity(this, content, getString(R.string.home_store_detail));
                        break;
                    case PUSH_WEBVIEW:
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.EXTRA_URL, content);
                        intent.putExtra(WebViewActivity.EXTRA_TITLE, getIntent().getStringExtra(EXTRA_WEB_TITLE));
                        startActivity(intent);
                        break;
                    case PUSH_LOGISTIC:
                        LogisticsDetailActivity.startLogisticsDetailActivity(this, content);
                        break;
                    default: {
                        Intent intentMain = new Intent(LauncherActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        break;
                    }
                }
            }
        }
    }

    private boolean isNewVersion() {
        versionCode = VersionInfoUtils.getVersionCode(this);
        return ConfigUtils.getOldVersionCode(this) < versionCode;
    }

    @Override
    public void onBackPressed() {
        if (canBack) {
            super.onBackPressed();
        }
    }
}
