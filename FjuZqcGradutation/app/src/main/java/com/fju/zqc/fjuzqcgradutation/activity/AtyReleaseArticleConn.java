package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConn;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowWrite;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhang on 2015/12/22.
 */
public class AtyReleaseArticleConn extends BaseFragmentActivity {
    @BindView
    private CustomTitleView tvTitle;
    @BindView
    private EditText etTitle;
    @BindView
    private EditText etContent;
    @BindView
    private TextView tvTextNum;
    private int restNumber;
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_release_article_conn;
    }

    @Override
    protected void initView() {
        editTextListener();
        tvTitle.setTitle("创建接龙文章");
        tvTitle.setTxtLeftText("       ");
        tvTitle.setTxtRightIcon(R.drawable.complate_write);
        tvTitle.setTxtRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveArticle();
            }
        });
        tvTitle.setTxtLeftIcon(R.drawable.header_back);
        tvTitle.setTxtLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        etContent.setBackground(ColorTheme.getEditorDrawable());
        etTitle.setBackground(ColorTheme.getEditorDrawable());
    }

    @Override
    protected void initData() {

    }

    /**
     * 保存文章接龙
     */
    public void saveArticle(){
        if(etTitle.getText().toString().trim().length()<=0
                ||etContent.getText().toString().trim().length()<=0){
            WidgetUtils.showToast("标题和内容不能为空!");
            return;
        }
        final DlgLoading dlgLoading=new DlgLoading(mContext);
        dlgLoading.show("发布中...");
        UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
        String articleTitle=etTitle.getText().toString().trim();
        String articleContent=etContent.getText().toString().trim();
        ArticleConn articleConn=new ArticleConn();
        articleConn.setArticleTitle(articleTitle);
        articleConn.setAddPersonNum(0);
        articleConn.setArticleContent(articleContent);
        articleConn.setIsWriting(0);
        articleConn.setUser(userInfo);
        articleConn.save(mContext,new SaveListener() {
            @Override
            public void onSuccess() {
                UserInfo userInfo = new UserInfo();
                userInfo.increment("vipNumber", 10);
                userInfo.update(mContext, DataStorageUtils.getPid(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dlgLoading.dismiss();
                        Log.d("tttt", "SUCCESS");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        dlgLoading.dismiss();
                        Log.d("tttt", i + "===ERROR====" + s);
                    }
                });
                dlgShowWriteSuccess();
            }
            @Override
            public void onFailure(int i, String s) {
                dlgLoading.dismiss();
                Toast.makeText(mContext,"发布失败!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 弹出发布成功提示
     */
    private void dlgShowWriteSuccess(){
        final DlgShowWrite dlgShowWrite=new DlgShowWrite(mContext);
        dlgShowWrite.getTvSeeOther().setText("去参加文章接龙");
        dlgShowWrite.show();
        dlgShowWrite.getRlWriteAgain().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etContent.setText("");
                etTitle.setText("");
                dlgShowWrite.dismiss();
            }
        });
        dlgShowWrite.getRlSeeOther().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AtyReleaseArticleConn.this,AtyArticleConnection.class));
                finish();
            }
        });
    }
    private void editTextListener(){
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (restNumber <= 200) {
                    restNumber = s.length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTextNum.setText(restNumber + "");
            }
        });
    }
}
