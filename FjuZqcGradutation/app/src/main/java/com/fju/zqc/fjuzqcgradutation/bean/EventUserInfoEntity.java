package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by zhang on 2016/3/28.
 */
public class EventUserInfoEntity {
    private String birth;
    private String phone;
    private String sex;
    private String school;
    private String sign;
    private boolean isShowPhone;

    public EventUserInfoEntity(String birth, String phone, String sex
            , String school, String sign,boolean isShowPhone) {
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
        this.school = school;
        this.sign = sign;
        this.isShowPhone=isShowPhone;
    }

    public boolean isShowPhone() {
        return isShowPhone;
    }

    public void setShowPhone(boolean isShowPhone) {
        this.isShowPhone = isShowPhone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
