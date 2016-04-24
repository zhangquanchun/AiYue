package com.fju.zqc.fjuzqcgradutation;

import android.app.Application;

import com.cy.imagelib.ImageLibInitializer;
import com.cy.widgetlibrary.WidgetLibInitializer;
import com.fju.zqc.fjuzqcgradutation.utils.AppUtils;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ejianshen on 15/10/14.
 */
public class ZqcApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLibInitializer.init(this);
        AppUtils.init(this);
        WidgetLibInitializer.init(this);
        com.hhtech.base.AppUtils.init(this);
        ShareSDK.initSDK(this,"b2bd4f1931ce");
        Bmob.initialize(this,"87f352223f3b6486ced2934dcab90235");
    }


}
