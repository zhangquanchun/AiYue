package com.fju.zqc.fjuzqcgradutation.net;

import android.content.Context;
import android.os.Bundle;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.content.DlgLoading;

import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by zhang on 2015/10/18.
 */
public class LogInByAuthorize {
    private Handler handler;
    private Context context;
    private DlgLoading dlgLoading;
    public LogInByAuthorize(Handler handler,Context context) {
        this.handler=handler;
        this.context=context;
        dlgLoading=new DlgLoading(context);
    }


}
