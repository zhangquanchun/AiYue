package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.AuthorListAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.AuthorListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.MyArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.db.AuthorDao;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenu;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuCreator;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuItem;
import com.fju.zqc.fjuzqcgradutation.swipemenulistview.SwipeMenuListView;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.fju.zqc.fjuzqcgradutation.view.SelectArticleTypeView;
import com.hhtech.utils.DimenUtils;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ejianshen on 15/10/12.
 */
public class AtyAuthorArticle extends BaseFragmentActivity implements SwipeMenuListView.ISwipeLoadListener {
    @BindView
    private View rlNoArticle,rlSearch;
    @BindView
    private ImageView ivWriteArticle,ivSelect,ivImageClear;
    @BindView
    private TextView tvNotify;
    @BindView
    private SwipeMenuListView listView;
    @BindView
    private CustomTitleView tvTitle;
    private android.os.Handler handler=new android.os.Handler();
    @BindView
    private AutoCompleteTextView etSearch;
    private List<String> listArticleAuthor=new ArrayList<String>(Arrays.asList(AtyWriteArticle.ARTICLE_TYPE));
    private DlgLoading dlgLoading;
    private int currentPageNum;
    private final static int MINPAGENUM=15;
    private static int loadNum=0;
    private List<String> searchHistory=new ArrayList<>();
    private AuthorListAdapter authorListAdapter;
    private List<AuthorListEntity> authorListEntities=new ArrayList<>();
    private AuthorListEntity authorListEntity;
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_author_article;
    }
    @Override
    protected void initView() {
        listArticleAuthor.add("全部");
        searchArticle();
        tvTitle.setTitle("Ta们的文章");
        tvTitle.setTxtLeftText("       ");
        rlSearch.setBackground(ColorTheme.getEditorDrawable());
        tvTitle.setTxtLeftIcon(R.drawable.header_back);
        tvTitle.setTxtLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectItem();
            }
        });
        ivWriteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DataStorageUtils.getIsLogIn()){
                    startActivity(new Intent(AtyAuthorArticle.this,AtyWriteArticle.class));
                }else {
                    final DlgShowIsLogIn dlgShowIsLogIn = new DlgShowIsLogIn(AtyAuthorArticle.this);
                    dlgShowIsLogIn.show();
                    dlgShowIsLogIn.getRlGoLogIn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(AtyAuthorArticle.this, AtyLogIn.class));
                            finish();
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
        });
        ivImageClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
            }
        });
        initAuthor();
    }

    /**
     * 初始化容器数据
     */
    private void initAuthor(){
       AuthorDao authorDao=new AuthorDao(mContext);
        List<String> findResult=new ArrayList<>();
        findResult=authorDao.findAllAuthor();
        for(String author:findResult){
            searchHistory.add(author);
        }
    }
    /**
     * 通过作者搜索文章
     */
    private void searchArticle(){
           etSearch.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

               }
               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                   if (charSequence!=null&&!charSequence.toString().trim().isEmpty()){
                       ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(mContext
                               ,R.layout.my_dropdown_item_1line
                               ,searchHistory);
                       etSearch.setAdapter(arrayAdapter);
                       ivImageClear.setVisibility(View.VISIBLE);
                       BmobQuery<ArticleList> query=new BmobQuery<ArticleList>();
                       query.setLimit(30);
                       query.addWhereContains("articleTitle",charSequence.toString());
                       query.include("user");
                       query.order("-createdAt");
                       query.findObjects(mContext,new FindListener<ArticleList>() {
                           @Override
                           public void onSuccess(List<ArticleList> list) {
                               authorListAdapter.clear();
                               authorListEntities.clear();
                               for(int i=0;i<list.size();i++){
                                   String time=list.get(i).getCreatedAt();
                                   String currentTime=format.format(new Date(System.currentTimeMillis()));
                                   String articleTime= GetTimeFormat.getTimeFormat(currentTime, time);
                                   authorListEntity=new AuthorListEntity(list.get(i).getUser().getFid()
                                           ,list.get(i).getArticleTitle()
                                           ,list.get(i).getArticleContent()
                                           ,articleTime
                                           ,list.get(i).getUser().getUsername());
                                   authorListEntity.setCommentNum(list.get(i).getCommentNum());
                                   authorListEntity.setPid(list.get(i).getUser().getObjectId());
                                   authorListEntity.setCollectNum(list.get(i).getCollectNum());
                                   authorListEntity.setArticleId(list.get(i).getObjectId());
                                   authorListEntity.setArticleList(list.get(i));
                                   authorListEntities.add(authorListEntity);
                               }
                               if(authorListEntities.size()<=0){
                                   rlNoArticle.setVisibility(View.VISIBLE);
                                   tvNotify.setText("没有搜索到指定文章，请重新输入");
                               }else{
                                   rlNoArticle.setVisibility(View.GONE);
                               }
                               if(authorListAdapter==null){
                                   authorListAdapter=new AuthorListAdapter(mContext,authorListEntities);
                                   listView.setAdapter(authorListAdapter);
                               }else{
                                   authorListAdapter.notifyDataSetChanged();
                               }
                           }
                           @Override
                           public void onError(int i, String s) {

                           }
                       });
                   }else{
                       getArticleList(0,true);
                       ivImageClear.setVisibility(View.GONE);
                   }

               }
               @Override
               public void afterTextChanged(Editable editable) {

               }
           });
    }

    @Override
    protected void initData() {
        getArticleList(0,false);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(ColorTheme.getColor(R.color.blue)));
                // set item width
                openItem.setWidth(DimenUtils.dpToPx(90));
                // set item title
                openItem.setTitle("评论");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
                AuthorListEntity entity = authorListEntities.get(position);
                switch (index) {
                    case 0:
                        if (DataStorageUtils.getIsLogIn()){
                            Intent intent=new Intent(mContext,AtyComment.class);
                            IntentToContentEntity intentEntity=new IntentToContentEntity(entity.getArticleTitle()
                                    ,entity.getArticleId()
                                    ,entity.getContent()
                                    ,entity.getAuthor()
                                    ,entity.getImageUrl()
                                    ,true
                                    ,entity.getPid(),entity.getArticleList());
                            intent.putExtra(IntentToContentEntity.INTENT_KEY, intentEntity);
                            startActivity(intent);
                        }else{
                            noLogIn();
                        }

                        break;
                }
            }
        });
    }

    /**
     * 获取文章列表
     */
    private void getArticleList(final int pageNum,boolean isSearch){
        //如果来自搜索，则清除适配器和容器
        if (isSearch){
            authorListEntities.clear();
            authorListAdapter.clear();
        }
        dlgLoading=new DlgLoading(mContext);
        listView.setInterface(this);
        BmobQuery<ArticleList> query=new BmobQuery<>();
        query.setLimit(MINPAGENUM);
        query.setSkip(pageNum);
        query.include("user");
        query.order("-createdAt");
        if(pageNum==0)
        dlgLoading.show("拼命加载中....");
        query.findObjects(this, new FindListener<ArticleList>() {
            @Override
            public void onSuccess(List<ArticleList> list) {
                currentPageNum=pageNum+MINPAGENUM;
                loadNum=list.size();
                for(int i=0;i<list.size();i++){
                    String time=list.get(i).getCreatedAt();
                    String currentTime=format.format(new Date(System.currentTimeMillis()));
                    String articleTime= GetTimeFormat.getTimeFormat(currentTime, time);
                    authorListEntity=new AuthorListEntity(list.get(i).getUser().getFid()
                            ,list.get(i).getArticleTitle()
                            ,list.get(i).getArticleContent()
                            ,articleTime
                            ,list.get(i).getUser().getUsername());
                    authorListEntity.setCommentNum(list.get(i).getCommentNum());
                    authorListEntity.setPid(list.get(i).getUser().getObjectId());
                    authorListEntity.setCollectNum(list.get(i).getCollectNum());
                    authorListEntity.setArticleId(list.get(i).getObjectId());
                    authorListEntity.setArticleList(list.get(i));
                    authorListEntity.setUserInfo(list.get(i).getUser());
                    authorListEntities.add(authorListEntity);
                }
                if(authorListEntities.size()<0&&pageNum==0){
                    tvNotify.setText("还没有人写作，点击右下角图标可以创作哦!");
                    rlNoArticle.setVisibility(View.VISIBLE);
                }else{
                    rlNoArticle.setVisibility(View.GONE);
                }
                if(authorListAdapter==null){
                    authorListAdapter=new AuthorListAdapter(mContext,authorListEntities);
                    listView.setAdapter(authorListAdapter);
                }else{
                    authorListAdapter.notifyDataSetChanged();
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                authorListEntity=authorListEntities.get(i);
                Intent intent=new Intent(AtyAuthorArticle.this,AtyAuthorContent.class);
                IntentToContentEntity intentToContent=new IntentToContentEntity(authorListEntity.getArticleTitle()
                        ,authorListEntity.getArticleId()
                        ,authorListEntity.getContent()
                        ,authorListEntity.getAuthor()
                        ,authorListEntity.getImageUrl()
                        ,true,authorListEntity.getPid(),authorListEntity.getArticleList());
                intent.putExtra(IntentToContentEntity.INTENT_KEY,intentToContent);
                startActivity(intent);
            }
        });
    }

    /**
     * 弹出筛选框
     */
    private void getSelectItem(){
        final SelectArticleTypeView dlgShow=new SelectArticleTypeView(mContext);
        ListView selectListView=dlgShow.getListView();
        dlgShow.show();
        SelectAdapter adapter=new SelectAdapter(mContext,listArticleAuthor);
        selectListView.setAdapter(adapter);
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectItem=listArticleAuthor.get(position);
                getSelectTypeArticle(selectItem);
                dlgShow.dismiss();
            }
        });
    }

    /**
     * 选取指定类型的文章
     * @param selectItem
     */
    private void getSelectTypeArticle(String selectItem){
        BmobQuery<ArticleList> query=new BmobQuery<>();
        if(selectItem!=null&&!selectItem.equals("全部"))
        query.addWhereEqualTo("articleType",selectItem);
        query.setLimit(100);
        query.include("user");
        query.order("-createdAt");
        query.findObjects(mContext,new FindListener<ArticleList>() {
            @Override
            public void onSuccess(List<ArticleList> list) {
                authorListAdapter.clear();
                authorListEntities.clear();
                for(int i=0;i<list.size();i++){
                    String time=list.get(i).getCreatedAt();
                    String currentTime=format.format(new Date(System.currentTimeMillis()));
                    String articleTime= GetTimeFormat.getTimeFormat(currentTime, time);
                    authorListEntity=new AuthorListEntity(list.get(i).getUser().getFid()
                            ,list.get(i).getArticleTitle()
                            ,list.get(i).getArticleContent()
                            ,articleTime
                            ,list.get(i).getUser().getUsername());
                    authorListEntity.setCommentNum(list.get(i).getCommentNum());
                    authorListEntity.setPid(list.get(i).getUser().getObjectId());
                    authorListEntity.setCollectNum(list.get(i).getCollectNum());
                    authorListEntity.setArticleId(list.get(i).getObjectId());
                    authorListEntity.setArticleList(list.get(i));
                    authorListEntities.add(authorListEntity);
                }
                if(authorListEntities.size()<=0){
                    rlNoArticle.setVisibility(View.VISIBLE);
                    tvNotify.setText("没有搜索到指定文章，请重新选择");
                }else{
                    rlNoArticle.setVisibility(View.GONE);
                }
                if(authorListAdapter==null){
                    authorListAdapter=new AuthorListAdapter(mContext,authorListEntities);
                    listView.setAdapter(authorListAdapter);
                }else{
                    authorListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    /**
     * 筛选适配器
     */
   public class SelectAdapter extends AdapterBase<String>{
       public SelectAdapter(Context context, List<String> list) {
           super(context, list);
       }
       @Override
       protected View getItemView(int position, View convertView
               , ViewGroup parent, String entity) {
           if(convertView==null)
               convertView=mInflater.inflate(R.layout.item_select_article,null);
           ((TextView)getHolderView(convertView,R.id.tvTextView)).setText(entity);
           return convertView;
       }
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
    /**
     * 下拉刷新
     */
    @Override
    public void onLoad() {
        if(loadNum<MINPAGENUM){
            Toast.makeText(mContext,"没有更多文章",Toast.LENGTH_SHORT).show();
            listView.LoadComplate();
           return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    getArticleList(currentPageNum,false);
                    listView.LoadComplate();
            }
        },2000);
    }
}
