package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.cy.widgetlibrary.view.PromotedActionsLibrary;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.CollectArticle;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.utils.FileUtils;
import com.fju.zqc.fjuzqcgradutation.utils.StringUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowSaveOk;
import com.fju.zqc.fjuzqcgradutation.view.Share2WeiXin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by ejianshen on 15/10/12.
 */
public class AtyAuthorContent extends BaseFragmentActivity {
    @BindView
    private View rlNoArticle;
    @BindView
    private TextView tvTitle,tvContent
            ,tvAuthor,batterLevel,tvTime,totalTxtSize;
    @BindView
    private View rlBack,llBack,rlSetBg;
    @BindView
    private FrameLayout frameLayout;
    @BindView
    private ScrollView scrollView;
    @BindView
    private ImageView ivBack,ivBattery,ivSetBg;
    private String authorPid;
    private String articleTitle,author,imageUrl,articleContent,articleId;
    private boolean isShow=false;
    private boolean isCollect=false;
    private boolean isWhite=false;
    private  PromotedActionsLibrary promotedActionsLibrary;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
    private ArticleList articleList;
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.aty_author_content;
    }

    @Override
    protected void initView() {
        batteryLevel();
        setTime();
        promotedActionsLibrary=new PromotedActionsLibrary();
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    rlBack.setVisibility(View.GONE);
                    promotedActionsLibrary.hideView(false);
                    isShow = false;
                } else {
                    promotedActionsLibrary.hideView(true);
                    rlBack.setVisibility(View.VISIBLE);
                    isShow = true;
                }
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlSetBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlBack.setVisibility(View.GONE);
                isShow=false;
                promotedActionsLibrary.hideView(false);
                if (isWhite) {
                    frameLayout.setBackgroundResource(R.drawable.article_background);
                    isWhite = false;
                } else {
                    frameLayout.setBackgroundResource(R.drawable.article_content);
                    isWhite = true;
                }
            }
        });
    }

    @Override
    protected void initData() {
        final Intent intent=getIntent();
        if(intent!=null){
            IntentToContentEntity entity=(IntentToContentEntity)
                    intent.getSerializableExtra(IntentToContentEntity.INTENT_KEY);
            articleTitle=entity.title;
            author=entity.author;
            imageUrl=entity.imageUrl;
            articleContent=entity.content;
            articleId=entity.articleId;
            isCollect=entity.isCollect;
            authorPid=entity.pid;
            tvTitle.setText(articleTitle);
            articleList=entity.articleList;
            totalTxtSize.setText("总字数:"+articleContent.length());
            tvContent.setText(articleContent);
            tvAuthor.setText("作者:"+author);
        }
        promotedActionsLibrary.setup(mContext, frameLayout);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataStorageUtils.getIsLogIn()){
                    noLogIn();
                }else{
                    if (intent!=null){
                        intent.setClass(AtyAuthorContent.this,AtyComment.class);
                        startActivity(intent);
                    }
                }


            }
        };
        View.OnClickListener onClickListener1=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataStorageUtils.getIsLogIn()){
                    noLogIn();
                }else{
                    BmobQuery<CollectArticle> query=new BmobQuery<>();
                    query.addWhereEqualTo("pid", DataStorageUtils.getPid());
                    query.findObjects(mContext, new FindListener<CollectArticle>() {
                        @Override
                        public void onSuccess(List<CollectArticle> list) {
                            boolean isExist = false;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getArticleList().getObjectId().equals(articleId)) {
                                    isExist = true;
                                }
                            }
                            if (isExist) {
                                WidgetUtils.showToast("你已经收藏了该文章!");
                            } else {
                                addArticle();
                            }
                        }
                        @Override
                        public void onError(int i, String s) {

                        }
                    });

                }

            }
        };
        View.OnClickListener onClickListener2=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataStorageUtils.getIsLogIn()){
                    noLogIn();
                }else{
                    save2File();
                }

            }
        };
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.icon_comment_add), onClickListener);
        if(isCollect)
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
//                    SpannableStringBuilder stringBuilder= StringUtils.creSpanString(new String[]{"正在充电:" + level + "%"}
//                            , new int[]{ColorTheme.getColor(R.color.blue)}, new int[]{12});
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
    /**分享至微信*/
    private void shareToWeiXin(){
        Share2WeiXin share2WeiXin=new Share2WeiXin(mContext);
        share2WeiXin.getTvUserName().setText(articleTitle);
        share2WeiXin.getTvAuthor().setText(author);
        ImageLoaderUtils.getInstance().loadImage(imageUrl, share2WeiXin.getIvImage());
        share2WeiXin.getShare2WeiXin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setTitle(articleTitle);
                sp.setText(articleContent);
                sp.setImageUrl(imageUrl);
                sp.setShareType(Platform.SHARE_TEXT);

                Platform qzone = ShareSDK.getPlatform(Wechat.NAME);
                qzone. setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.d("tttt","complete");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("tttt","error"+throwable.getCause()+throwable.getMessage());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Log.d("tttt","cancel");
                    }
                }); // 设置分享事件回调
                // 执行图文分享
                qzone.share(sp);
            }
        });
        share2WeiXin.getShare2Sina().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setText(articleTitle);
                sp.setImagePath(imageUrl);

                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
                        Log.d("tttt","complete");
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.d("tttt","error"+throwable.getMessage());
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                }); // 设置分享事件回调
// 执行图文分享
                weibo.share(sp);
            }
        });
        share2WeiXin.show();
    }
    /**收藏文章*/
    private void addArticle(){
        DlgTextMsg dlgTextMsg=new DlgTextMsg(mContext, new DlgTextMsg.ConfirmDialogListener() {
            @Override
            public void onOk(DlgTextMsg dlg) {
                CollectArticle collectArticle=new CollectArticle();
                final DlgLoading dlgLoading=new DlgLoading(mContext);
                dlgLoading.show();
                UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
                collectArticle.setArticleList(articleList);
                collectArticle.setType("1");
                collectArticle.setUser(userInfo);
                collectArticle.save(mContext, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ArticleList articleList=new ArticleList();
                        articleList.increment("collectNum", 1);
                        articleList.update(mContext, articleId, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                WidgetUtils.showToast("收藏成功!");
                                dlgLoading.dismiss();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                WidgetUtils.showToast("收藏失败!");
                                dlgLoading.dismiss();
                            }
                        });
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
    /**保存文章为文本文件*/
    private void save2File(){
        DlgTextMsg dlgTextMsg=new DlgTextMsg(mContext, new DlgTextMsg.ConfirmDialogListener() {
            @Override
            public void onOk(DlgTextMsg dlg) {
                String path=FileUtils.createDir(FileUtils.createRootPath()
                        + "/" + articleTitle + ".txt");
                if(path!=null){
                     FileUtils.writeFileSdcard(path, articleContent, false);
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
