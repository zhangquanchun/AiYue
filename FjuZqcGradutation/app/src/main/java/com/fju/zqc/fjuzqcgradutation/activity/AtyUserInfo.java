package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.EventUserInfoEntity;
import com.fju.zqc.fjuzqcgradutation.bean.Intent2Setting;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.BusEventListener;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowImage;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import de.greenrobot.event.EventBus;

/**
 * Created by zhang on 2016/3/26.
 */
public class AtyUserInfo extends BaseFragmentActivity {
    @BindView
    private RelativeLayout rl_base_info;
    @BindView
    private ImageView ivBack;
    @BindView
    private ImageView iv_to_setting;
    @BindView
    private static CustomCircleImageView iv_header;
    @BindView
    private static TextView tv_sex;
    @BindView
    private static TextView tv_age;
    @BindView
    private static TextView tv_school;
    @BindView
    private TextView tv_nickname;
    @BindView
    private TextView tv_live;
    @BindView
    private static TextView tv_phone;
    @BindView
    private static RelativeLayout rl_phone;
    @BindView
    private static TextView tv_sign;
    @BindView
    private TextView tv_article_tag;
    @BindView
    private RelativeLayout rl_2_article;
    @BindView
    private LinearLayout rlUserInfo;
    @BindView
    private static TextView tv_birth;
    private String pid;
    private UserInfo user;
    private final static int MIN_SIZE=6;
    private int number;
    private int level=0;
    private static SimpleDateFormat format=new SimpleDateFormat("yyyy");
    private UpdateInfo updateInfo=new UpdateInfo();
    private UpdateHead updateHead=new UpdateHead();
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
        return R.layout.aty_user_info;
    }

    @Override
    protected void initView() {
        WindowManager vm=(WindowManager)mContext.getSystemService(WINDOW_SERVICE);
        int height=vm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams info=(LinearLayout.LayoutParams)rl_base_info.getLayoutParams();
        info.height=height*2/5;
        iv_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AtySetInfo.class);
                intent.putExtra(Intent2Setting.KEY,new Intent2Setting(user));
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DlgShowImage dlgShowImage=new DlgShowImage(mContext);
                ImageLoaderUtils.getInstance().loadImage(user.getFid(),dlgShowImage.getIv_max_image());
                dlgShowImage.getIv_max_image().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlgShowImage.dismiss();
                    }
                });
                dlgShowImage.show();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        pid=intent.getStringExtra("pid");
        if (pid.equals(DataStorageUtils.getPid())){
            tv_article_tag.setText("我的文章");
            iv_to_setting.setVisibility(View.VISIBLE);
        }
        rl_2_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(mContext,AtyMyArticle.class);
                intent1.putExtra(Intent2Setting.KEY,new Intent2Setting(user));
                startActivity(intent1);
            }
        });
        BmobQuery<UserInfo> query=new BmobQuery<>();
        query.getObject(mContext,pid,new GetListener<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                user=userInfo;
                if (userInfo.getFid()!=null){
                    ImageLoaderUtils.getInstance().loadImage(userInfo.getFid(),iv_header);
                }else{
                    iv_header.setImageResource(R.drawable.ic_launcher);
                }
                if (userInfo.getBirth_day()!=null){
                    String age=userInfo.getBirth_day().substring(0,4);
                    int birthYear=Integer.parseInt(age);
                    long date=System.currentTimeMillis();
                    String currentTime=format.format(new Date(date));
                    int currentYear=Integer.parseInt(currentTime);
                    int userAge=currentYear-birthYear;
                    tv_age.setText(userAge+"岁");
                }else{
                    tv_age.setText("年龄");
                }
                tv_sex.setText(userInfo.getSex()==null?"性别":userInfo.getSex());
                tv_school.setText(userInfo.getSchool()==null?"学校":userInfo.getSchool());
                tv_nickname.setText(userInfo.getUsername());
                number=userInfo.getVipNumber();
                vipLevel(MIN_SIZE);
                tv_live.setText(level+"");
                tv_birth.setText(userInfo.getBirth_day()==null?"无":userInfo.getBirth_day());
                if (userInfo.getIs_show_phone()!=null&&userInfo.getIs_show_phone()){
                    rl_phone.setVisibility(View.VISIBLE);
                    tv_phone.setText(userInfo.getMobilePhoneNumber()==null?"无":userInfo.getMobilePhoneNumber());
                }else {
                    rl_phone.setVisibility(View.GONE);
                }
                tv_sign.setText(userInfo.getSign()==null?"无":userInfo.getSign());
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        EventBus.getDefault().register(updateHead);
        EventBus.getDefault().register(updateInfo);
    }
    private void vipLevel(int size){
        if (number<Math.pow(2,size)){
            level=size-MIN_SIZE;
        }else{
            vipLevel(size+1);
        }
    }
    private final static class UpdateInfo implements BusEventListener
            .MainThreadListener<EventUserInfoEntity>{
        @Override
        public void onEventMainThread(EventUserInfoEntity event) {
            String age=event.getBirth().substring(0, 4);
            int birthYear=Integer.parseInt(age);
            long date=System.currentTimeMillis();
            String currentTime=format.format(new Date(date));
            int currentYear=Integer.parseInt(currentTime);
            int userAge=currentYear-birthYear;
            tv_age.setText(userAge+"岁");
            tv_sex.setText(event.getSex());
            tv_school.setText(event.getSchool());
            tv_birth.setText(event.getBirth());
            if (event.isShowPhone()){
                rl_phone.setVisibility(View.VISIBLE);
                tv_phone.setText(event.getPhone());
            }else {
                rl_phone.setVisibility(View.GONE);
            }
            tv_sign.setText(event.getSign());
        }
    }
    private final static class UpdateHead implements BusEventListener.MainThreadListener<String>{
        @Override
        public void onEventMainThread(String event) {
            ImageLoaderUtils.getInstance().loadImage(event,iv_header);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(updateInfo);
        EventBus.getDefault().unregister(updateHead);
        super.onDestroy();
    }
}
