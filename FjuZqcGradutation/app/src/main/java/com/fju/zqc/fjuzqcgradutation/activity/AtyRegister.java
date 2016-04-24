package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.utils.BgDrawableUtils;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zqc on 15/10/22.
 */
public class AtyRegister extends BaseFragmentActivity implements View.OnClickListener{

    @BindView
    private EditText editPhone,editPsw;
    @BindView
    private CustomTitleView vTitle;
    @BindView
    private Button btnRegis,btn_toLogin;
    @BindView
    private ImageView btnDelete;
    @BindView
    private EditText edit_sure_psw;
    private StateListDrawable bgCode;
    private DlgLoading dlgLoading;

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
        return R.layout.aty_regis;
    }

    @Override
    protected void initView() {
        vTitle.setTitle("注册账号");
        bgCode = BgDrawableUtils.crePressSelector(0xff009dd9, 0xff058abd, 5);
        StateListDrawable bgRegis = BgDrawableUtils.crePressSelector(
                0xff009dd9, 0xff058abd, 5);
        btnRegis.setOnClickListener(this);
        btn_toLogin.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        editPhone.addTextChangedListener(textWatcher);
        editPhone.setBackground(ColorTheme.getEditorDrawable());
        editPsw.setBackground(ColorTheme.getEditorDrawable());
        edit_sure_psw.setBackground(ColorTheme.getEditorDrawable());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegis:
                if (checkInput()){
                    onRegister();
                }

                break;
            case R.id.btn_toLogin:
                startActivity(new Intent(AtyRegister.this,AtyLogIn.class));
                finish();
                break;
            case R.id.btnDelete:
                editPhone.getText().clear();
                break;
        }
    }

    private void onRegister(){
        dlgLoading=new DlgLoading(mContext);
        final String username=editPhone.getText().toString().trim();
        final String psw=editPsw.getText().toString().trim();
        BmobQuery<UserInfo> query=new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        dlgLoading.show("正在注册....");
        query.findObjects(this, new FindListener<UserInfo>() {
            @Override
            public void onSuccess(List<UserInfo> list) {
                if(list.size()<=0){
                    dlgLoading.dismiss();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUsername(username);
                    userInfo.setPassword(psw);
                    userInfo.signUp(AtyRegister.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            WidgetUtils.showToast("恭喜，注册成功");
                            dlgLoading.dismiss();
                            Intent intent=new Intent(AtyRegister.this, AtyLogIn.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            dlgLoading.dismiss();
                            WidgetUtils.showToast(" 额,注册失败了");
                        }
                    });
                }else{
                    dlgLoading.dismiss();
                    WidgetUtils.showToast("用户名已经存在!");
                }
            }

            @Override
            public void onError(int i, String s) {
               dlgLoading.dismiss();
            }
        });

    }
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            // TODO Auto-generated method stub
            editPhone.setSelected(false);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            String phone = arg0.toString();
            btnDelete.setVisibility(TextUtils.isEmpty(phone) ? View.GONE
                    : View.VISIBLE);
        }
    };
    private boolean checkInput(){
        String username=editPhone.getText().toString().trim();
        String psw=editPsw.getText().toString().trim();
        String sure_psw=edit_sure_psw.getText().toString().trim();
        if (username.isEmpty()){
            WidgetUtils.showToast("请输入用户名");
            return  false;
        }else if(psw.isEmpty()){
            WidgetUtils.showToast("请输入密码");
            return  false;
        }else if(!psw.equals(sure_psw)){
            WidgetUtils.showToast("两次密码不一样");
            return  false;
        }else {
            return true;
        }
    }

}
