package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.utils.GetTimeFormat;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.adapter.CommentAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.CommentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.CommentList;
import com.fju.zqc.fjuzqcgradutation.bean.IntentToContentEntity;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.PopupEditWindows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ejianshen on 15/11/6.
 */
public class AtyComment extends BaseFragmentActivity {
    @BindView
    private TextView tvAuthor,articleContent,tvTitle;
    @BindView
    private ImageView ivHeader,ivComment;
    private PopupEditWindows popupEditWindows;
    @BindView
    private AddListViewMore listView;
    private EditText editText;
    private boolean isComment=false;
    private String articleTitle,author,imageUrl,content,articleId;
    @BindView
    private View rlComment;
    private CommentEntity commentEntity;
    private String authorPid;
    private List<CommentEntity> commentEntities=new ArrayList<>();
    private CommentAdapter commentAdapter;
    private ArticleList article_list;
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
        return R.layout.aty_comment;
    }
    @Override
    protected void initView() {
        Intent intent=getIntent();
        if(intent!=null){
            IntentToContentEntity entity=(IntentToContentEntity)
                    intent.getSerializableExtra(IntentToContentEntity.INTENT_KEY);
            articleTitle=entity.title;
            author=entity.author;
            imageUrl=entity.imageUrl;
            content=entity.content;
            articleId=entity.articleId;
            authorPid=entity.pid;
            tvTitle.setText(articleTitle);
            articleContent.setText(content);
            article_list=entity.articleList;
            ImageLoaderUtils.getInstance().loadImage(imageUrl,ivHeader);
            tvAuthor.setText(author);
        }
        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEditWindows=new PopupEditWindows(mActivity,onClickListener);
                editText=popupEditWindows.getEditComment();
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(editText.getText().toString().trim().length()<=0){
                            WidgetUtils.showToast("评论内容不能为空!");
                            return false;
                        }
                        if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            final DlgLoading dlgLoading=new DlgLoading(mContext);
                            dlgLoading.show("提交评论中...");
                            final UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
                            CommentList commentList=new CommentList();
                            commentList.setArticleList(article_list);
                            commentList.setUser(userInfo);
                            commentList.setCommentMsg(editText.getText().toString().trim());
                            commentList.save(mContext, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    final ArticleList articleList=new ArticleList();
                                    articleList.increment("commentNum",1);
                                    articleList.update(mContext, articleId, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                                commentEntity=new CommentEntity(DataStorageUtils.getCurUserProfileFid()
                                                        ,"刚刚"
                                                        ,userInfo.getUsername()
                                                        ,editText.getText().toString().trim());
                                                commentEntities.add(commentEntity);
                                            if(commentAdapter!=null){
                                                commentAdapter.notifyDataSetChanged();
                                            }else {
                                                commentAdapter=new CommentAdapter(mContext,commentEntities);
                                                listView.setAdapter(commentAdapter);
                                            }
                                            dlgLoading.dismiss();
                                            WidgetUtils.showToast("恭喜，评论成功!");
                                        }
                                        @Override
                                        public void onFailure(int i, String s) {
                                            dlgLoading.dismiss();
                                            WidgetUtils.showToast("抱歉，评论失败！");
                                        }
                                    });
                                }
                                @Override
                                public void onFailure(int i, String s) {
                                    dlgLoading.dismiss();
                                    WidgetUtils.showToast("抱歉，评论失败！");
                                }
                            });
                            popupEditWindows.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                //显示窗口
                popupEditWindows.showAtLocation(findViewById(R.id.rlComment),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    protected void initData() {
        getCommentList();
    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private void getCommentList(){
        BmobQuery<CommentList> query=new BmobQuery<>();
        query.addWhereEqualTo("articleList",article_list);
        query.order("-createdAt");
        query.include("articleList,user");
        query.findObjects(mContext, new FindListener<CommentList>() {
            @Override
            public void onSuccess(List<CommentList> list) {
                for(int i=0;i<list.size();i++){
                    String time=list.get(i).getCreatedAt();
                    String currentTime=format.format(new Date(System.currentTimeMillis()));
                    String articleTime= GetTimeFormat.getTimeFormat(currentTime, time);
                    CommentList commentList=list.get(i);
                    commentEntity=new CommentEntity(commentList.getUser().getFid()
                            ,articleTime
                            ,commentList.getUser().getUsername()
                            ,commentList.getCommentMsg());
                    commentEntities.add(commentEntity);
                }
                if (commentAdapter==null){
                    commentAdapter=new CommentAdapter(mContext,commentEntities);
                    listView.setAdapter(commentAdapter);
                }else{
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
                //startActivity(new Intent(this,AtyAuthorArticle.class));
                finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
