package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.cy.widgetlibrary.view.PromotedActionsLibrary;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.CollectArticle;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.jsoup.GetArticleContent;
import com.fju.zqc.fjuzqcgradutation.net.GetWebArticle;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.utils.FileUtils;
import com.fju.zqc.fjuzqcgradutation.utils.StringUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowSaveOk;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by zqc on 2015/9/9.
 */
public class AtyArticleView extends BaseFragmentActivity {
    @BindView
    private TextView tvTitle,batterLevel,tvTime,totalTxtSize;
    @BindView
    private ListView lvArticle;
    @BindView
    private WebView articleView;
    @BindView
    private FrameLayout llView;
    @BindView
    private View rlBack,llBack,rlSetBg;
    @BindView
    private ImageView ivBack,ivBattery,ivSetBg;
    private Handler handler=new Handler();
    private GetArticleContent getArticleContent;
    private boolean isWhite=false;
    private PromotedActionsLibrary promotedActionsLibrary;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
    private String title,author,content,imageUrl,articleUrl;
    private ArticleList articleList;
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_artivle_view;
    }

    @Override
    protected void initView() {
       batteryLevel();
        setTime();
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlSetBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWhite) {
                    llView.setBackgroundResource(R.drawable.article_background);
                    isWhite = false;
                } else {
                    llView.setBackgroundResource(R.drawable.article_content);
                    isWhite = true;
                }
            }
        });
    }

    @Override
    protected void initData(){
        promotedActionsLibrary=new PromotedActionsLibrary();
        getArticleContent=new GetArticleContent(this,handler,tvTitle
                ,lvArticle,totalTxtSize,rlBack,llBack);
        Intent intent=getIntent();
        final String url=intent.getStringExtra("url");
        content=intent.getStringExtra("content");
        title=intent.getStringExtra("title");
        author=intent.getStringExtra("author");
        imageUrl=intent.getStringExtra("imageUrl");
        articleUrl=intent.getStringExtra("url");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String content= GetWebArticle.getWebArticle(url);
                    getArticleContent.getDetailArticle(content);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        View.OnClickListener onClickListener1=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataStorageUtils.getIsLogIn()){
                    noLogIn();
                }else{
                     collectArticle();
                }
            }
        };
        View.OnClickListener onClickListener2=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataStorageUtils.getIsLogIn()){
                    noLogIn();
                }else{
                    saveArticle2File();
                }

            }
        };
        promotedActionsLibrary.setup(mContext, llView);
        //promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.icon_comment), onClickListener);
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.icon_collect_add), onClickListener1);
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.icon_save_add), onClickListener2);
        promotedActionsLibrary.addMainItem(getResources().getDrawable(R.drawable.icon_add));

    }
    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra("level", -1);//获得当前电量
                int scale = intent.getIntExtra("scale", -1); //获得总电量
                int status = intent.getIntExtra("status", -1); //获取当前状态
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                if(BatteryManager.BATTERY_STATUS_CHARGING==status){
//                   SpannableStringBuilder stringBuilder= StringUtils.creSpanString(new String[]{"正在充电:"+level+"%"}
//                           ,new int[]{ColorTheme.getColor(R.color.blue)},new int[]{12});
                    ivBattery.setBackgroundResource(R.drawable.icon_battery);
                }else{
                    if (level<30){
                        ivBattery.setBackgroundResource(R.drawable.icon_battery1);
                    }else if(level>30&&level<60){
                        ivBattery.setBackgroundResource(R.drawable.icon_battery2);
                    }else if(level>60&&level<99){
                        ivBattery.setBackgroundResource(R.drawable.icon_battery3);
                    }else{
                        ivBattery.setBackgroundResource(R.drawable.icon_battery4);
                    }
                }

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
    private void setTime(){
        long time=System.currentTimeMillis();
        String systemTime=simpleDateFormat.format(new Date(time));
        tvTime.setText(systemTime);
    }
    /**
     * 收藏文章
     */
    private void collectArticle(){
        DlgTextMsg dlgTextMsg=new DlgTextMsg(mContext, new DlgTextMsg.ConfirmDialogListener() {
            @Override
            public void onOk(DlgTextMsg dlg) {
                CollectArticle collectArticle=new CollectArticle();
                final DlgLoading dlgLoading=new DlgLoading(mContext);
                dlgLoading.show();
                UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
                collectArticle.setType("0");
                collectArticle.setImageUrl(imageUrl);
                collectArticle.setContent(content);
                collectArticle.setAuthor(author);
                collectArticle.setTitle(title);
                collectArticle.setUrl(articleUrl);
                collectArticle.setUser(userInfo);
                collectArticle.save(mContext, new SaveListener() {
                    @Override
                    public void onSuccess() {
                                WidgetUtils.showToast("收藏成功!");
                                dlgLoading.dismiss();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        WidgetUtils.showToast("收藏失败!");
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
        dlgTextMsg.setBtnString("收藏", "不用");
        dlgTextMsg.show("收藏此文章？");
    }
    /**
     *  保存文章
     */
    private void saveArticle2File(){
        DlgTextMsg dlgTextMsg=new DlgTextMsg(mContext, new DlgTextMsg.ConfirmDialogListener() {
            @Override
            public void onOk(DlgTextMsg dlg) {
                String path= FileUtils.createFileDir(FileUtils.createRootPath()
                        + "/" + title + ".txt");
                if(path!=null){
                    FileUtils.writeFileSdcard(path, content, false);
                    final DlgShowSaveOk showSaveOk=new DlgShowSaveOk(mContext);
                    showSaveOk.getTvUserName().setText("文章路径:"+path);
                    showSaveOk.getRlReadOn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSaveOk.dismiss();
                        }
                    });
                    showSaveOk.getRlBack().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    showSaveOk.show();
                }else{
                    WidgetUtils.showToast("该文章已经存在!");
                }
            }
            @Override
            public void onCancel() {

            }
        });
        dlgTextMsg.setBtnString("保存","不用");
        dlgTextMsg.show("保存此文章至本地?");
    }
    private void noLogIn(){
        final DlgShowIsLogIn dlgShowIsLogIn = new DlgShowIsLogIn(mContext);
        dlgShowIsLogIn.show();
        dlgShowIsLogIn.getRlGoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
                startActivity(new Intent(mContext, AtyLogIn.class));
            }
        });
        dlgShowIsLogIn.getRlNoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
            }
        });
    }
}
