package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.fju.zqc.fjuzqcgradutation.adapter.CollectListAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenu;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuCreator;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuItem;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuListView;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.AuthorListAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.AuthorListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.CollectArticle;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;

import com.fju.zqc.fjuzqcgradutation.view.DlgIsDelete;
import com.hhtech.utils.DimenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by ejianshen on 15/10/22.
 */
public class AtyCollectArticle extends BaseFragmentActivity implements SwipeMenuListView.ISwipeLoadListener {
    @BindView
    private SwipeMenuListView listView;
    @BindView
    private CustomTitleView tvTitle;
    @BindView
    private View rlNoArticle;
    @BindView
    private ImageView ivWriteArticle;
    @BindView
    private TextView tvNotify;
    private CollectListAdapter authorListAdapter;
    private List<AuthorListEntity> authorListEntities=new ArrayList<>();
    private AuthorListEntity authorListEntity;
    private DlgLoading dlgLoading;
    private int currentPageNum;
    private final static int MINPAGENUM=15;
    private android.os.Handler handler=new android.os.Handler();
    private static int loadNum=0;
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
        return R.layout.aty_collect_article;
    }

    @Override
    protected void initView() {
        tvTitle.setTitle("我的收藏");
        tvTitle.setTxtLeftText("       ");
        tvTitle.setTxtLeftIcon(R.drawable.header_back);
        tvTitle.setTxtLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        getArticleList(0);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
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
               final AuthorListEntity articleListEntity = authorListEntities.get(position);
                switch (index) {
                    case 0:
                        final DlgIsDelete dlgIsDelete=new DlgIsDelete(mContext);
                        dlgIsDelete.show();
                        dlgIsDelete.getRl_delete().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CollectArticle collectArticle = new CollectArticle();
                                collectArticle.setObjectId(articleListEntity.getObjectId());
                                collectArticle.delete(mContext, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        authorListEntities.remove(position);
                                        authorListAdapter.notifyDataSetChanged();
                                        dlgIsDelete.dismiss();
                                        WidgetUtils.showToast("移除文章成功!");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        WidgetUtils.showToast("移除文章失败!" + s);
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
    private void getArticleList(final int pageNum){
        dlgLoading=new DlgLoading(mContext);
        listView.setInterface(this);
        UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
        BmobQuery<CollectArticle> query=new BmobQuery<>();
        query.setLimit(MINPAGENUM);
        query.setSkip(pageNum);
        query.order("createdAt");
        query.addWhereEqualTo("user", userInfo);
        query.include("articleList,user,articleList.user");
        if(pageNum==0)
        dlgLoading.show("拼命加载中....");
        query.findObjects(this, new FindListener<CollectArticle>() {
            @Override
            public void onSuccess(final List<CollectArticle> list) {
                loadNum=list.size();
                currentPageNum = pageNum + MINPAGENUM;
                for (int i = 0; i < list.size(); i++) {
                    CollectArticle item=list.get(i);
                    if(list.get(i).getType().equals("1")){
                        String time = list.get(i).getCreatedAt();
                        String currentTime = format.format(new Date(System.currentTimeMillis()));
                        String articleTime = GetTimeFormat.getTimeFormat(currentTime, time);
                        authorListEntity=new AuthorListEntity(item.getArticleList().getUser().getFid()
                                ,item.getArticleList().getArticleTitle()
                                ,item.getArticleList().getArticleContent()
                                ,"收藏时间:"+articleTime
                                ,"作者:"+item.getArticleList().getUser().getUsername());
                        authorListEntity.setArticleId(item.getArticleList().getObjectId());
                        authorListEntity.setCommentNum(item.getArticleList().getCommentNum());
                        authorListEntity.setCollectNum(item.getArticleList().getCollectNum());
                        authorListEntity.setType("1");
                        authorListEntity.setObjectId(item.getObjectId());
                        authorListEntity.setArticleList(item.getArticleList());
                        authorListEntities.add(authorListEntity);
                        if(authorListAdapter==null){
                            authorListAdapter=new CollectListAdapter(mContext,authorListEntities);
                            listView.setAdapter(authorListAdapter);
                        }else{
                            authorListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        String currentTime = format.format(new Date(System.currentTimeMillis()));
                        String articleTime = GetTimeFormat.getTimeFormat(currentTime,list.get(i).getCreatedAt());
                        authorListEntity=new AuthorListEntity(list.get(i).getImageUrl()
                                ,list.get(i).getTitle()
                                ,list.get(i).getContent()
                                ,"收藏时间:"+articleTime
                                ,list.get(i).getAuthor());
                        authorListEntity.setCommentNum(0);
                        authorListEntity.setArticleUrl(list.get(i).getUrl());
                        authorListEntity.setCollectNum(0);
                        authorListEntity.setType("0");
                        authorListEntity.setObjectId(item.getObjectId());
                        authorListEntities.add(authorListEntity);
                        if(authorListAdapter==null){
                            authorListAdapter=new CollectListAdapter(mContext,authorListEntities);
                            listView.setAdapter(authorListAdapter);
                        }else{
                            authorListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if(list.size()>0){
                    rlNoArticle.setVisibility(View.GONE);
                }else{
                    tvNotify.setText("你还没有收藏文章!");
                    ivWriteArticle.setVisibility(View.GONE);
                    rlNoArticle.setVisibility(View.VISIBLE);
                }

                dlgLoading.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                if(authorListEntities.size()>0){
                    rlNoArticle.setVisibility(View.GONE);
                }else{
                    rlNoArticle.setVisibility(View.VISIBLE);
                    ivWriteArticle.setVisibility(View.GONE);
                    tvNotify.setText("加载出现异常!");
                }
                dlgLoading.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                authorListEntity = authorListEntities.get(position);
                if(authorListEntity.getType().equals("1")){
                    Intent intent = new Intent(mContext, AtyAuthorContent.class);
                    IntentToContentEntity intentToContent=new IntentToContentEntity(authorListEntity.getArticleTitle()
                            ,authorListEntity.getArticleId()
                            ,authorListEntity.getContent()
                            ,authorListEntity.getAuthor()
                            ,authorListEntity.getImageUrl()
                            ,false,"0",authorListEntity.getArticleList());
                    intent.putExtra(IntentToContentEntity.INTENT_KEY,intentToContent);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(mContext,AtyArticleView.class);
                    intent.putExtra("url", authorListEntity.getArticleUrl());
                    intent.putExtra("title",authorListEntity.getArticleTitle());
                    intent.putExtra("author",authorListEntity.getAuthor());
                    intent.putExtra("content",authorListEntity.getContent());
                    intent.putExtra("imageUrl",authorListEntity.getImageUrl());
                    startActivity(intent);
                }

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

}
