package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.ArticleConnAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConn;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConnEntity;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToConnEntity;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgArticleNotify;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ejianshen on 15/10/22.
 */
public class AtyArticleConnection extends BaseFragmentActivity implements AddListViewMore.ILoadListener {


    @BindView
    private CustomTitleView tvTitle;
    @BindView
    private ImageView ivRelease;
    @BindView
    private AddListViewMore listView;
    private android.os.Handler handler=new android.os.Handler();
    private int loadNum;
    private int currentNum=0;
    private final static int PAGE_NUM=15;
    private DlgArticleNotify dlgShowNotify;
    private DlgLoading dlgLoading;
    @BindView
    private View rlNoArticleConn;
    @BindView
    private TextView tvNotify;
    private ArticleConnEntity articleConnEntity;
    private List<ArticleConnEntity> articleConnEntities=new ArrayList<>();
    private ArticleConnAdapter adapter;
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_article_conn;
    }

    @Override
    protected void initView() {
        dlgLoading=new DlgLoading(mContext);
        tvTitle.setTitle("文章接龙");
        tvTitle.setTxtLeftText("       ");
        tvTitle.setTxtLeftIcon(R.drawable.header_back);
        tvTitle.setTxtLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        ivRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataStorageUtils.getIsLogIn()){
                    startActivity(new Intent(mContext,AtyReleaseArticleConn.class));
                }else{
                    noLogIn();
                }

            }
        });
        showNotify();
        getArticleConn(0);
        listViewClick();

    }
    @Override
    protected void initData() {

    }

    /**
     * 判断是否已经登录
     */
    private void noLogIn(){
        final DlgShowIsLogIn dlgShowIsLogIn = new DlgShowIsLogIn(mContext);
        dlgShowIsLogIn.show();
        dlgShowIsLogIn.getRlGoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
                startActivity(new Intent(AtyArticleConnection.this, AtyLogIn.class));
            }
        });
        dlgShowIsLogIn.getRlNoLogIn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgShowIsLogIn.dismiss();
            }
        });
    }
    /**
     * 获取接龙文章列表
     */
    private void getArticleConn(final int skipNum){
        listView.setInterface(this);
        BmobQuery<ArticleConn> query=new BmobQuery<>();
        query.setLimit(PAGE_NUM);
        query.setSkip(skipNum);
        query.include("user");
        query.order("-createdAt");
        currentNum=skipNum+PAGE_NUM;
        query.findObjects(mContext,new FindListener<ArticleConn>() {
            @Override
            public void onSuccess(List<ArticleConn> list) {
                String currentTime=format.format(new Date(System.currentTimeMillis()));
                for(ArticleConn articleConn:list){
                    String nowTime=articleConn.getCreatedAt();
                    String articleTime= GetTimeFormat.getTimeFormat(currentTime,nowTime);
                    articleConnEntity=new ArticleConnEntity(articleConn.getArticleTitle()
                            ,articleConn.getArticleContent(),articleConn.getUser().getUsername()
                            ,articleConn.getIsWriting(),articleTime
                            ,articleConn.getObjectId(),articleConn.getUser().getFid()
                            ,articleConn.getAddPersonNum());
                    articleConnEntity.setArticleConn(articleConn);
                    articleConnEntity.setArticleId(articleConn.getObjectId());
                    articleConnEntity.setUserInfo(articleConn.getUser());
                    articleConnEntities.add(articleConnEntity);
                }
              if(adapter==null){
                  adapter=new ArticleConnAdapter(mContext,articleConnEntities);
                  listView.setAdapter(adapter);
              }else{
                  adapter.notifyDataSetChanged();
              }
                loadNum=list.size();
                dlgLoading.dismiss();
                if(list.size()<=0&&skipNum==0){
                    rlNoArticleConn.setVisibility(View.VISIBLE);
                }else {
                    rlNoArticleConn.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(int i, String s) {
                dlgLoading.dismiss();
                rlNoArticleConn.setVisibility(View.VISIBLE);
                tvNotify.setText("加载异常!");
            }
        });
    }

    /**
     * 列表点击跳转
     */
    private void listViewClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ArticleConnEntity entity = articleConnEntities.get(position);
                Intent intent = new Intent(mContext,AtyArticleConnNext.class);
                IntentToConnEntity intentToContentEntity=new
                        IntentToConnEntity(entity.getArticleTitle()
                        ,entity.getArticleId(),entity.getArticleContent()
                        ,entity.getAuthorName(),entity.getImageUrl(),false,"",entity.getArticleConn());
                intent.putExtra(IntentToConnEntity.INTENT_KEY,intentToContentEntity);
                startActivity(intent);
            }
        });
    }
    /**
     * 弹出提示
     */
    private void showNotify(){
        if(!DataStorageUtils.getIsShowNotify()){
            dlgShowNotify=new DlgArticleNotify(mContext);
            dlgShowNotify.show();
            dlgShowNotify.getIbtOk().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dlgShowNotify.getIbtNoShowAgain().isChecked()){
                        DataStorageUtils.setIsShowNotify(true);
                    }dlgShowNotify.dismiss();
                }
            });
        }else{
            dlgLoading.show("加载中...");
        }
    }

    @Override
    public void onLoad() {
        if(loadNum<PAGE_NUM){
            Toast.makeText(mContext, "没有更多文章", Toast.LENGTH_SHORT).show();
            listView.LoadComplate();
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getArticleConn(currentNum);
                listView.LoadComplate();
            }
        }, 2000);
    }
}
