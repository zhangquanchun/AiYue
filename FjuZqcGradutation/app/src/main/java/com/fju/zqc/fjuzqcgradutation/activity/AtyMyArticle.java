package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.effect.EffectFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.ArticleListAdapter;
import com.fju.zqc.fjuzqcgradutation.adapter.MyArticleAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.AuthorListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.CollectArticle;
import com.fju.zqc.fjuzqcgradutation.bean.Intent2Setting;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.MyArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenu;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuCreator;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuItem;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuListView;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgIsDelete;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.hhtech.utils.DimenUtils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ejianshen on 15/10/22.
 */
public class AtyMyArticle extends BaseFragmentActivity implements SwipeMenuListView.ISwipeLoadListener {
    @BindView
    private SwipeMenuListView listView;
    @BindView
    private CustomTitleView tvTitle;
    @BindView
    private View rlNoArticle;
    @BindView
    private ImageView ivPen;
    @BindView
    private ImageView ivWriteArticle;
    @BindView
    private TextView tvNotify;
    private MyArticleAdapter adapter;
    private MyArticleListEntity articleListEntity;
    private DlgLoading dlgLoading;
    private int currentPageNum;
    private final static int MINPAGENUM=15;
    private static int loadNum=0;
    private android.os.Handler handler=new android.os.Handler();
    private  UserInfo userInfo;;
    private List<MyArticleListEntity> articleListEntityList=new ArrayList<>();
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        return R.layout.aty_my_article;
    }

    @Override
    protected void initView() {

        tvTitle.setTxtLeftText("       ");
        tvTitle.setTxtLeftIcon(R.drawable.header_back);
        tvTitle.setTxtLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        ivWriteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,AtyWriteArticle.class));
            }
        });

    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        Intent2Setting intent2Setting=(Intent2Setting)intent
                .getSerializableExtra(Intent2Setting.KEY);
        userInfo=intent2Setting.user;
        if (userInfo.getObjectId().equals(DataStorageUtils.getPid())){
            tvTitle.setTitle("我的文章");
            deleteArticle();
            ivPen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AtyMyArticle.this, AtyWriteArticle.class));
                    finish();
                }
            });
        }else{
            ivPen.setVisibility(View.GONE);
            if (userInfo.getSex()!=null&&userInfo.getSex().equals("男")){
                tvTitle.setTitle("他的文章");
            }else{
                tvTitle.setTitle("她的文章");
            }

        }
        getArticleList(0);

    }
    private void getArticleList(final int pageNum){
        dlgLoading=new DlgLoading(mContext);
        listView.setInterface(this);
//        UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
        BmobQuery<ArticleList> query=new BmobQuery<>();
        query.setLimit(MINPAGENUM);
        query.setSkip(pageNum);
        query.order("-createdAt");
        query.addWhereEqualTo("user",userInfo);
        query.include("user");
        if(pageNum==0)
        dlgLoading.show("拼命加载中....");
        query.findObjects(this, new FindListener<ArticleList>() {
            @Override
            public void onSuccess(List<ArticleList> list) {
                loadNum=list.size();
                currentPageNum = pageNum + MINPAGENUM;
                for (int i = 0; i < list.size(); i++) {
                    String time=list.get(i).getCreatedAt();
                    String currentTime=format.format(new Date(System.currentTimeMillis()));
                    String articleTime= GetTimeFormat.getTimeFormat(currentTime,time);
                    articleListEntity = new MyArticleListEntity(list.get(i).getObjectId()
                            ,list.get(i).getArticleTitle()
                            , list.get(i).getArticleContent()
                            , articleTime
                            , list.get(i).getUser().getUsername());
                    articleListEntity.setCommentNum(list.get(i).getCommentNum());
                    articleListEntity.setCollectNum(list.get(i).getCollectNum());
                    articleListEntity.setImageUrl(list.get(i).getUser().getFid());
                    articleListEntity.setArticleList(list.get(i));
                    articleListEntityList.add(articleListEntity);
                }
                if(articleListEntityList.size()>0){
                    rlNoArticle.setVisibility(View.GONE);
                }else{
                    if (userInfo.getObjectId().equals(DataStorageUtils.getPid())){
                        tvNotify.setText("你还没有写作，点击右下角图标可以写作!");
                        rlNoArticle.setVisibility(View.VISIBLE);
                    }else{
                        tvNotify.setText("他还没有写文章哦~");
                    }

                }
                if (adapter == null) {
                    adapter = new MyArticleAdapter(mContext, articleListEntityList);
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                dlgLoading.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                if(articleListEntityList.size()>0){
                    rlNoArticle.setVisibility(View.GONE);
                }else{
                    tvNotify.setText("加载出现异常!");
                    rlNoArticle.setVisibility(View.VISIBLE);
                }
                dlgLoading.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                articleListEntity = articleListEntityList.get(position);
                Intent intent = new Intent(mContext, AtyAuthorContent.class);
                IntentToContentEntity intentToContent=new IntentToContentEntity(articleListEntity.getArticleTitle()
                        ,articleListEntity.getObjectId()
                        ,articleListEntity.getArticleContent()
                        ,articleListEntity.getArticleAuthor()
                        ,DataStorageUtils.getCurUserProfileFid()
                        ,false,"0",articleListEntity.getArticleList());
                intent.putExtra(IntentToContentEntity.INTENT_KEY,intentToContent);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoad() {
        if(loadNum<MINPAGENUM){
            Toast.makeText(mContext, "没有更多文章", Toast.LENGTH_SHORT).show();
            listView.LoadComplate();
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getArticleList(currentPageNum);
                listView.LoadComplate();
            }
        },2000);
    }
    private void deleteArticle(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(ColorTheme.getColor(R.color.white)));
                // set item width
                deleteItem.setWidth(DimenUtils.dpToPx(90));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete_blue);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
               final MyArticleListEntity articleListEntity = articleListEntityList.get(position);
                switch (index) {
                    case 0:
                        final DlgIsDelete dlgIsDelete=new DlgIsDelete(mContext);
                        dlgIsDelete.show();
                        dlgIsDelete.getRl_delete().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArticleList articleList = new ArticleList();
                                articleList.setObjectId(articleListEntity.getObjectId());
                                articleList.delete(mContext, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        articleListEntityList.remove(position);
                                        adapter.notifyDataSetChanged();
                                        WidgetUtils.showToast("删除文章成功!");
                                        dlgIsDelete.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        WidgetUtils.showToast("删除文章失败!");
                                        dlgIsDelete.dismiss();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        });
    }
}
