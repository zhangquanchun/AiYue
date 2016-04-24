package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.ArticleConnNextAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConn;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConnNext;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleNextSupport;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToConnEntity;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowIsLogIn;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowWrite;
import com.fju.zqc.fjuzqcgradutation.view.PopupEditWindows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.volley.toolbox.ImageLoader;

/**
 * Created by zhang on 2015/12/23.
 */
public class AtyArticleConnNext extends BaseFragmentActivity implements
        AddListViewMore.ILoadListener {

    @BindView
    private RoundCImageView ivHeader;
    @BindView
    private TextView tvTitle;
    @BindView
    private TextView articleContent;
    @BindView
    private TextView tvAuthor;
    @BindView
    private View llWrite;
    @BindView
    private AddListViewMore listView;
    @BindView
    private ImageView ivBack;
    private ArticleConnNext articleConnNext;
    private List<ArticleConnNext> articleConnNexts=new ArrayList<>();
    private ArticleConnNextAdapter adapter;
    private android.os.Handler handler=new android.os.Handler();
    private final static int PAGE_NUM=15;
    private int currentNum=0;
    private int loadNum=0;
    private PopupEditWindows popupEditWindows;
    private String articleId;
    private boolean isWrite,isJoin,isAdd=true;
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ArticleConn article_conn;
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_article_conn_next;
    }

    @Override
    protected void initView() {
        listView.setInterface(this);
        Intent intent=getIntent();
        IntentToConnEntity entity=(IntentToConnEntity)intent.getSerializableExtra(
                IntentToConnEntity.INTENT_KEY);
        articleId=entity.articleId;
        ImageLoaderUtils.getInstance().loadImage(entity.imageUrl, ivHeader);
        tvTitle.setText(entity.title);
        tvAuthor.setText(entity.author);
        articleContent.setText(entity.content);
        article_conn=entity.articleConn;
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

        getArticleConn(currentNum);
        addArticleConn();
    }

    /**
     * 加入接龙
     */
    private void addArticleConn(){
        llWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isJoin){
                  return;
                }
                isJoin=true;
                if(DataStorageUtils.getIsLogIn()){
                    checkIsWriting();
                }else{
                    noLogIn();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isJoin=false;
                    }
                },4000);

            }
        });

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
                startActivity(new Intent(AtyArticleConnNext.this, AtyLogIn.class));
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
     * 检查是否有人正在写
     */
    private void checkIsWriting(){
        BmobQuery<ArticleConn> query=new BmobQuery<>();
        query.getObject(mContext,articleId,new GetListener<ArticleConn>() {
            @Override
            public void onSuccess(ArticleConn articleConn) {
                if(articleConn.getIsWriting()>0){
                    final DlgShowWrite dlgShowWrite=new DlgShowWrite(mContext);
                    dlgShowWrite.show();
                    dlgShowWrite.getTvSeeOther().setText("等会写");
                    dlgShowWrite.getTvWriteOther().setText("继续写");
                    dlgShowWrite.getIvImage().setImageResource(R.drawable.icon_sure);
                    dlgShowWrite.getTvUserName().setText("有"+articleConn.getIsWriting()+"个人");
                    dlgShowWrite.getTvDetail().setText("正在写接龙，您是否要继续写？");
                    dlgShowWrite.getRlWriteAgain().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                              writeArticleConn();
                            dlgShowWrite.dismiss();
                        }
                    });
                    dlgShowWrite.getRlSeeOther().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dlgShowWrite.dismiss();
                        }
                    });
                }else{
                    writeArticleConn();
                }
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(mContext,"加入失败!",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 写接龙
     */
    private void writeArticleConn(){
        popupEditWindows=new PopupEditWindows(mActivity,new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final EditText editText=popupEditWindows.getEditComment();
        editTextListener(editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if(editText.getText().toString().trim().length()<=20){
                    WidgetUtils.showToast("文章内容不能小于20字!");
                    return false;
                }
                if (actionId == EditorInfo.IME_ACTION_DONE || (
                        event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    final DlgLoading dlgLoading=new DlgLoading(mContext);
                    dlgLoading.show("正在提交...");
                    ArticleConnNext articleConnNext2=new ArticleConnNext();
                    final UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
                    articleConnNext2.setUser(userInfo);
                    articleConnNext2.setArticleConn(article_conn);
                    articleConnNext2.setSupportNum(0);
                    articleConnNext2.setArticleContent(editText.getText().toString().trim());
                    articleConnNext2.save(mContext,new SaveListener() {
                        @Override
                        public void onSuccess() {
                            final ArticleConn articleConn=new ArticleConn();
                            articleConn.increment("isWriting", -1);
                            articleConn.increment("addPersonNum",1);
                            articleConn.update(mActivity,articleId,new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    articleConnNext=new ArticleConnNext(editText.getText().toString().trim()
                                            ,0,userInfo,"刚刚",articleConn);
                                    articleConnNexts.add(articleConnNext);
                                if (adapter==null){
                                    adapter=new ArticleConnNextAdapter(mContext,articleConnNexts);
                                    listView.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.increment("vipNumber", 3);
                                    userInfo.update(mContext, article_conn.getUser().getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            dlgLoading.dismiss();
                                            Toast.makeText(mContext,"成功接龙!",Toast.LENGTH_SHORT).show();
                                            Log.d("tttt", "SUCCESS");
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            dlgLoading.dismiss();
                                            Log.d("tttt", i + "===ERROR====" + s);
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    dlgLoading.dismiss();
                                    Toast.makeText(mContext,"接龙失败!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onFailure(int i, String s) {
                        }
                    });
                    popupEditWindows.dismiss();
                    return true;
                }
                return false;
            }
        });
        isAdd=true;
        popupEditWindows.showAtLocation(findViewById(R.id.rlComment),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private void editTextListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (isAdd&&charSequence.length()>0){
                    isAdd=false;
                    final View mMenuView=popupEditWindows.getmMenuView();
                    mMenuView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                            int y=(int) event.getY();
                            if(event.getAction()== MotionEvent.ACTION_UP){
                                if(y<height){
                                    WidgetUtils.showToast("您还没写完哦!");
                                }
                            }
                            return true;
                        }
                    });
                    ArticleConn articleConn1=new ArticleConn();
                    articleConn1.increment("isWriting",1);
                    articleConn1.update(mActivity,articleId,new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
                if (charSequence.length()<=0){
                    ArticleConn articleConn1=new ArticleConn();
                    articleConn1.increment("isWriting",-1);
                    articleConn1.update(mActivity,articleId,new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            popupEditWindows.dismiss();
                        }
                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 获取其他作者对该文章的接龙
     */
    private void getArticleConn(int skipNum){
        final DlgLoading dlgLoading=new DlgLoading(mContext);
        if(skipNum==0){
           dlgLoading.show("正在加载...");
        }
        final BmobQuery<ArticleConnNext> query=new BmobQuery<>();
        query.setLimit(PAGE_NUM);
        query.setSkip(skipNum);
        query.order("-createdAt");
        query.include("user,articleConn");
        query.addWhereEqualTo("articleConn",article_conn);
        query.findObjects(mContext,new FindListener<ArticleConnNext>() {
            @Override
            public void onSuccess(List<ArticleConnNext> list) {
               final String currentTime=format.format(new Date(System.currentTimeMillis()));
                for(final ArticleConnNext item:list){
                    String nowTime=item.getCreatedAt();
                            String articleTime= GetTimeFormat.getTimeFormat(currentTime, nowTime);
                            articleConnNext=new ArticleConnNext(item.getArticleContent()
                                    ,item.getSupportNum()
                                    ,item.getUser()
                                    ,articleTime,article_conn);
                            articleConnNext.setObjectId(item.getObjectId());
                            articleConnNexts.add(articleConnNext);
                            if (adapter==null){
                                adapter=new ArticleConnNextAdapter(mContext,articleConnNexts);
                                listView.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                }
                dlgLoading.dismiss();
            }
            @Override
            public void onError(int i, String s) {
                dlgLoading.dismiss();
                Toast.makeText(mContext,"加载接龙文章失败!",Toast.LENGTH_SHORT).show();
            }
        });
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
