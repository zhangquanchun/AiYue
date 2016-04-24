package com.fju.zqc.fjuzqcgradutation.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.base.FragmentViewPagerAdapter;
import com.cy.widgetlibrary.content.DlgEdit;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.cy.widgetlibrary.view.CustomViewPager;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.SlidingTabs.SlidingTabLayout;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConn;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.Intent2Setting;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentAllArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentCoArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentLoveArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentStuArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentSuiBiArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentWebArticle;
import com.fju.zqc.fjuzqcgradutation.fragment.FragmentYcArticle;
import com.fju.zqc.fjuzqcgradutation.jsoup.GetArticleList;
import com.fju.zqc.fjuzqcgradutation.utils.BusEventListener;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.utils.PhotoUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.fju.zqc.fjuzqcgradutation.view.LogInWindows;
import com.fju.zqc.fjuzqcgradutation.view.PopupWindows;
import com.fju.zqc.sharelibrary.onekeyshare.OnekeyShare;
import com.hhtech.utils.NetIsAvailable;
import com.hhtech.utils.UITimer;
import com.mob.tools.utils.UIHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;


public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    SlidingTabLayout mSlidingTabLayout;
    @BindView
    private static CustomCircleImageView ivImageView;
    @BindView
    private View txtNavButton,rlLogIn,llAuthorA,llCollectA,llMyData,llArticleConn;
    @BindView
    private TextView username;
    @BindView
    private View rlNet,rlDrawerHeader;
    private CustomViewPager mViewPager;
    private FragmentViewPagerAdapter adapter;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private FragmentAllArticle ftAllArticle=new FragmentAllArticle();
    private long exitTime;
    private PopupWindows popupWindows;
    private DlgLoading dlgLoading;
    private LogInWindows logInWindows;
    private UITimer checkNetWork=new UITimer();
    private boolean isNoNet=false;

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fragments.add(new FragmentWebArticle());
        fragments.add(new FragmentYcArticle());
        fragments.add(new FragmentLoveArticle());
        fragments.add(new FragmentStuArticle());
        fragments.add(new FragmentSuiBiArticle());
        fragments.add(new FragmentCoArticle());
        rlLogIn.setOnClickListener(this);
        ivImageView.setOnClickListener(this);
        txtNavButton.setOnClickListener(this);
        llAuthorA.setOnClickListener(this);
        dlgLoading=new DlgLoading(this);
        llCollectA.setOnClickListener(this);
        llMyData.setOnClickListener(this);
        llArticleConn.setOnClickListener(this);
        rlDrawerHeader.setOnClickListener(this);
        checkNetWork.schedule(CheckNet,3000);

        if(DataStorageUtils.getIsLogIn()){
            username.setText(DataStorageUtils.getUserNickName());
        }else{
            username.setText("登录/注册");
        }
        //longClickLogIn();
    }

    UpdateHead updateHead=new UpdateHead();
    @Override
    protected void initData() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        /**注册事件监听*/
        EventBus.getDefault().register(updateHead);
        setSupportActionBar(mToolbar);
        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(6);
        adapter=new FragmentViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorMainDark));
        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(6); // tabcachesize (=tabcount for better performance)
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.net_article, R.string.net_article);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // use own style rules for tab layout
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_indicator_color));
        mSlidingTabLayout.setDistributeEvenly(true);

        adapter=new FragmentViewPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mSlidingTabLayout.setViewPager(mViewPager);

        // Tab events
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mViewPager.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        /**加载个人头像*/
          setImageHeader(DataStorageUtils.getCurUserProfileFid());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtNavButton:
                if(DataStorageUtils.getIsLogIn()){
                    UserInfo user= BmobUser.getCurrentUser(mContext,UserInfo.class);
                    Intent intent1=new Intent(mContext,AtyMyArticle.class);
                    intent1.putExtra(Intent2Setting.KEY,new Intent2Setting(user));
                    startActivity(intent1);
                }else {
                    noLogIn();
                }
                break;
            case R.id.llAuthorA:
                    startActivity(new Intent(MainActivity.this,AtyAuthorArticle.class));
                break;
            case R.id.llCollectA:
                if(DataStorageUtils.getIsLogIn()) {
                    startActivity(new Intent(MainActivity.this, AtyCollectArticle.class));
                }else{
                    noLogIn();
                }
                break;
            case R.id.llMyData:
                if (DataStorageUtils.getIsLogIn()){
                    startActivity(new Intent(MainActivity.this,AtyAWeekData.class));
                }else{
                    noLogIn();
                }

                break;
            case R.id.llArticleConn:
                startActivity(new Intent(MainActivity.this,AtyArticleConnection.class));
                break;
            case R.id.rlLogIn:
                logInWindows=new LogInWindows(this, itemsOnClickLogIn);
                Button btnSina=logInWindows.getBtnSina();
                Button btnWeiXin=logInWindows.getBtnWeiXin();
                Button btnARead=logInWindows.getBtnQQ();
                Button btnCancle=logInWindows.getBtnCancle();
                if(DataStorageUtils.getIsLogIn()){
                    btnSina.setVisibility(View.GONE);
                    btnWeiXin.setVisibility(View.GONE);
                    btnARead.setVisibility(View.GONE);
                    btnCancle.setText("切换账号");
                }else{
                    btnSina.setVisibility(View.GONE);
                    btnWeiXin.setVisibility(View.GONE);
                    btnARead.setVisibility(View.VISIBLE);
                    btnCancle.setText("取消");
                }
                    //显示窗口
                    logInWindows.showAtLocation(findViewById(R.id.drawer_layout),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.ivImageView:
                if (DataStorageUtils.getIsLogIn()){
                    Intent intent=new Intent(mContext,AtyUserInfo.class);
                    intent.putExtra("pid",DataStorageUtils.getPid());
                    startActivity(intent);
                }else{
                    noLogIn();
                }
                break;
            case R.id.rlDrawerHeader:
                if (DataStorageUtils.getIsLogIn()){
                    Intent intent=new Intent(mContext,AtyUserInfo.class);
                    intent.putExtra("pid",DataStorageUtils.getPid());
                    startActivity(intent);
                }else{
                    noLogIn();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,AtyAuthorArticle.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }


    /**登入弹窗*/
    private View.OnClickListener itemsOnClickLogIn = new View.OnClickListener(){

        public void onClick(View v) {
            logInWindows.dismiss();
            switch (v.getId()) {
                case R.id.SinaWeiBoLogIn:
                   sinaLogin();
                    break;
                case R.id.WeiXinLogIn:
                    // 微信
                    break;
                case R.id.AReadLogIn:
                    startActivity(new Intent(MainActivity.this,AtyLogIn.class));
                    break;
                case R.id.btn_cancel:
                    if(DataStorageUtils.getIsLogIn())
                    {
                        logInWindows.dismiss();
                        DataStorageUtils.setIsLogIn(false);
                        ivImageView.setImageResource(R.drawable.ic_launcher);
                        startActivity(new Intent(mActivity,AtyLogIn.class));
                        username.setText("登录/注册");
                    }else{
                        logInWindows.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }

    };
    /**
     * 新浪微博登录
     */
    private void sinaLogin() {
        dlgLoading.show("正在登录新浪微博,请稍候...");
        dlgLoading.dismiss();
    }
    /**
     * 双击退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit(){
        if(System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(this,"再次点击退出程序",Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            System.exit(0);
        }
    }
    private Runnable CheckNet=new Runnable() {
        @Override
        public void run() {
            if(NetIsAvailable.isNetworkConnected(MainActivity.this)){
                rlNet.setVisibility(View.GONE);
            }else{
                rlNet.setVisibility(View.VISIBLE);
            }
        }
    };
    private void noLogIn(){
        final DlgShowIsLogIn dlgShowIsLogIn = new DlgShowIsLogIn(MainActivity.this);
        dlgShowIsLogIn.show();
        dlgShowIsLogIn.getRlGoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
                startActivity(new Intent(MainActivity.this, AtyLogIn.class));
            }
        });
        dlgShowIsLogIn.getRlNoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkNetWork.schedule(CheckNet, 3000);
        if(DataStorageUtils.getIsLogIn()){
            username.setText(DataStorageUtils.getUserNickName());
        }else{
            username.setText("登录/注册");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        checkNetWork.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkNetWork.cancel();
        EventBus.getDefault().unregister(updateHead);
    }
    /**加载个人头像*/
    public static void setImageHeader(String url){
        if(url.isEmpty()||!DataStorageUtils.getIsLogIn()){
            ivImageView.setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance()
                    .loadImage(url
                            , ivImageView);
        }
    }
    private final static class UpdateHead implements BusEventListener.MainThreadListener<String>{
        @Override
        public void onEventMainThread(String event) {
            setImageHeader(event);
        }
    }
    private void longClickLogIn(){
        rlLogIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DlgEdit dlgEdit = new DlgEdit(mActivity, new DlgEdit.EditDialogListener() {
                    @Override
                    public void onOk(final String nickname) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUsername(nickname);
                        userInfo.update(mContext, DataStorageUtils.getPid(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                WidgetUtils.showToast("更新用户名成功!");
                                DataStorageUtils.setUserNickName(nickname);
                                username.setText(nickname);

                            }

                            @Override
                            public void onFailure(int i, String s) {
                                WidgetUtils.showToast("更新用户名失败!");
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                dlgEdit.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                dlgEdit.show("输入新的用户名(小于8个字)", null);
                return false;
            }
        });
    }

}
