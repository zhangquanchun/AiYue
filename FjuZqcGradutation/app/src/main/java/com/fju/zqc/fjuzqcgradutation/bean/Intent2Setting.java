package com.fju.zqc.fjuzqcgradutation.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2016/3/27.
 */
public class Intent2Setting implements Serializable {
    public UserInfo user;
    public final static String KEY="setting";
    public Intent2Setting(UserInfo user) {
        this.user = user;
    }
}
