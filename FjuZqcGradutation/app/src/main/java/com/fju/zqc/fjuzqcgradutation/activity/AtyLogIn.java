package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;

import org.json.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;

/**
 * Created by ejianshen on 15/10/22.
 */
public class AtyLogIn extends BaseFragmentActivity implements View.OnClickListener {

    @BindView
    private EditText editPhone,editPsw;
    @BindView
    private Button btnLogin,btnRegis;
    @BindView
    private CustomCircleImageView iv_image;
    private DlgLoading dlgLoading;
    @BindView
    private TextView tv_reset_psw;
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
        return R.layout.aty_login;
    }

    @Override
    protected void initView() {
        btnLogin.setOnClickListener(this);
        btnRegis.setOnClickListener(this);
        tv_reset_psw.setOnClickListener(this);
        iv_image.setImageResource(R.drawable.ic_launcher);
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        if(intent.getStringExtra("username")==null&&DataStorageUtils.getUserNickName()!=null){
            editPhone.setText(DataStorageUtils.getUserNickName());
        }else{
            editPhone.setText(intent.getStringExtra("username"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                onLogIn();
                break;
            case R.id.btnRegis:
                startActivity(new Intent(AtyLogIn.this,AtyRegister.class));
                finish();
                break;
            case R.id.tv_reset_psw:
                startActivity(new Intent(mContext,AtyGetLostPsw.class));
                break;
            default:
        }
    }

    private void onLogIn(){
        dlgLoading=new DlgLoading(mContext);
        String username=editPhone.getText().toString().trim();
        String psw=editPsw.getText().toString().trim();
        final UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(psw);
        dlgLoading.show("登录中...");
        userInfo.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                dlgLoading.dismiss();
                DataStorageUtils.setPid(userInfo.getObjectId());
                DataStorageUtils.setIsLogIn(true);
                DataStorageUtils.setUserNickName(userInfo.getUsername());
                BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
                query.getObject(mContext, userInfo.getObjectId(), new GetListener<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo userInfo) {
                        DataStorageUtils.setCurUserProfileFid(userInfo.getFid());
                        EventBus.getDefault().post(userInfo.getFid());
                    }
                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                WidgetUtils.showToast("登录成功!");
//                startActivity(new Intent(AtyLogIn.this,MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                dlgLoading.dismiss();
                WidgetUtils.showToast("登录失败!");
            }
        });

    }

}
