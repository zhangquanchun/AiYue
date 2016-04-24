package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.db.AuthorDao;
import com.hhtech.utils.NetIsAvailable;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ejianshen on 15/10/28.
 */
public class AtyStartView extends BaseFragmentActivity {
    @BindView
    private ImageView ivStartView;
    private AlphaAnimation alphaAnimation;
    private AnimationSet animationSet;
    private Handler handler=new Handler();
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
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        return R.layout.aty_start_view;
    }

    @Override
    protected void initView() {

        if(!NetIsAvailable.isNetworkConnected(mContext)){
            startActivity(new Intent(mActivity, MainActivity.class));
            finish();
        }
        DlgAnimation.animatorFadeIn(ivStartView).start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
                query.setLimit(1000);
                query.findObjects(mContext,new FindListener<UserInfo>() {
                    @Override
                    public void onSuccess(List<UserInfo> userInfos) {
                        AuthorDao authorDao=new AuthorDao(mContext);
                        authorDao.deleteAuthor();
                        for(UserInfo userInfo:userInfos){
                           authorDao.insertAuthor(userInfo.getUsername());
                        }
                        startActivity(new Intent(mActivity, MainActivity.class));
                        finish();
                    }
                    @Override
                    public void onError(int i, String s) {
                        Log.d("ttttt","ERROR"+s);
                    }
                });
            }
        },2000);

    }

    @Override
    protected void initData() {

    }
}
