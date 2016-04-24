package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.EventBusPhone;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import de.greenrobot.event.EventBus;

/**
 * Created by zhang on 2016/3/27.
 */
public class AtyGetLostPsw extends BaseFragmentActivity {
    @BindView
    private CustomTitleView vTitle;
    @BindView
    private EditText et_phone;
    @BindView
    private EditText et_verify,et_psw,et_sure_psw;
    @BindView
    private Button btn_sent_verify;
    private String phoneNum;
    private TimeCount mTiemTimeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_get_lost_psw;
    }

    @Override
    protected void initView() {
        vTitle.setTitle("找回密码");
        vTitle.setTxtRightIcon(R.drawable.complate_write);
        vTitle.setTxtRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInPut()){
                    resetPsw();
                }
            }
        });
        vTitle.setTxtLeftIcon(R.drawable.header_back);
        vTitle.setTxtLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_phone.setBackground(ColorTheme.getEditorDrawable());
        et_verify.setBackground(ColorTheme.getEditorDrawable());
        et_psw.setBackground(ColorTheme.getEditorDrawable());
        et_sure_psw.setBackground(ColorTheme.getEditorDrawable());
    }

    @Override
    protected void initData() {
        btn_sent_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    BmobSMS.requestSMSCode(mContext,phoneNum,"ARead",new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e==null){
                                WidgetUtils.showToast("发送成功");
                                mTiemTimeCount = new TimeCount(60000, 1000);
                                mTiemTimeCount.start();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 绑定手机号
     */
    private void resetPsw(){
        String verifyCode=et_verify.getText().toString().trim();
        String psw=et_psw.getText().toString().trim();
        BmobUser.resetPasswordBySMSCode(mContext,verifyCode,psw,new ResetPasswordByCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    WidgetUtils.showToast("修改密码成功");
                    startActivity(new Intent(mContext,AtyLogIn.class));
                     finish();
                }else{
                    WidgetUtils.showToast("修改密码失败");
                }
            }
        });
    }

    /**
     * 检查输入
     */
    private boolean checkInPut(){
        String phone=et_phone.getText().toString().trim();
        String verifyCode=et_verify.getText().toString().trim();
        String psw=et_psw.getText().toString().trim();
        String sure_psw=et_sure_psw.getText().toString().trim();
        if (phone.isEmpty()||!phone.equals(phoneNum)){
            WidgetUtils.showToast("手机号码不正确!");
            return false;
        }else if(verifyCode.isEmpty()){
            WidgetUtils.showToast("请输入验证码!");
            return false;
        }else if (psw.isEmpty()){
            WidgetUtils.showToast("请输入密码!");
            return false;
        }else if(!psw.equals(sure_psw)){
            WidgetUtils.showToast("两次密码不一样!");
            return false;
        }else{
            return true;
        }
    }
    /**
     * 验证手机号
     * @return
     */
    protected boolean validate(){
        phoneNum=null;
        String text=et_phone.getText().toString().trim();
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(170)|(18[0,3，5-9])|(14[5,7])|(178))\\d{8}$");
        Matcher m = p.matcher(text);
        if(text.length()==0){
            WidgetUtils.showToast("请输入手机号!");
            return false;
        }
        else if(!m.matches()){
            WidgetUtils.showToast("请输入正确的手机号码!");
            return false;
        }
        else{
            phoneNum=text;
            return true;
        }
    }
    //计时重发
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_sent_verify.setClickable(false);
            btn_sent_verify.setText(millisUntilFinished / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            btn_sent_verify.setText("获取验证码");
            btn_sent_verify.setClickable(true);
        }
    }
}
