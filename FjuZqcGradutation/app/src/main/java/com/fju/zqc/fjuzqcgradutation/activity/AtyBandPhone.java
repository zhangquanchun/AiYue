package com.fju.zqc.fjuzqcgradutation.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import de.greenrobot.event.EventBus;

/**
 * Created by zhang on 2016/3/27.
 */
public class AtyBandPhone extends BaseFragmentActivity {
    @BindView
    private CustomTitleView vTitle;
    @BindView
    private EditText et_phone;
    @BindView
    private EditText et_verify;
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
        return R.layout.aty_band_phone;
    }

    @Override
    protected void initView() {
        vTitle.setTitle("绑定手机号");
        vTitle.setTxtRightIcon(R.drawable.complate_write);
        vTitle.setTxtRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInPut()){
                    bandPhone();
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
    private void bandPhone(){
        String verifyCode=et_verify.getText().toString().trim();
        BmobSMS.verifySmsCode(mContext,phoneNum,verifyCode,new VerifySMSCodeListener(){
            @Override
            public void done(BmobException e) {
                if (e==null){
                    UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
                    userInfo.setMobilePhoneNumber(phoneNum);
                    userInfo.setMobilePhoneNumberVerified(true);
                    userInfo.update(mContext,userInfo.getObjectId(),new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new EventBusPhone(phoneNum));
                            finish();
                            WidgetUtils.showToast("绑定成功!");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            WidgetUtils.showToast("该手机号已经被绑定!");
                        }
                    });
                }else{
                    WidgetUtils.showToast("验证码错误!");
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
        if (phone.isEmpty()||!phone.equals(phoneNum)){
            WidgetUtils.showToast("手机号码不正确!");
            return false;
        }else if(verifyCode.isEmpty()){
            WidgetUtils.showToast("请输入验证码!");
            return false;
        }else {
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
