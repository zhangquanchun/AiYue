package com.fju.zqc.fjuzqcgradutation.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by ejianshen on 15/10/22.
 */
public class UserInfo extends BmobUser {

    private String fid;
    private String sex;
    private String school;
    private String birth_day;
    private String sign;
    private Boolean is_show_phone;
    private Integer vipNumber;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Boolean getIs_show_phone() {
        return is_show_phone;
    }

    public void setIs_show_phone(Boolean is_show_phone) {
        this.is_show_phone = is_show_phone;
    }

    public Integer getVipNumber() {
        return vipNumber;
    }

    public void setVipNumber(Integer vipNumber) {
        this.vipNumber = vipNumber;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
