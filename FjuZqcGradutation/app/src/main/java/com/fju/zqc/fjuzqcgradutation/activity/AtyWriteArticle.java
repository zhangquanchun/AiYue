package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.ColorTheme;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.view.DlgShowWrite;

import java.util.Calendar;
import java.util.TimeZone;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zqc on 2015/10/18.
 */
public class AtyWriteArticle extends BaseFragmentActivity {

    @BindView
    private EditText etContent,etTitle;
    @BindView
    private TextView tvTextNum;

    @BindView
    private CustomTitleView tvTitle;
    @BindView
    private Spinner articleType;
    private int restNumber;
    private DlgLoading dlgLoading;
    public static final String[] ARTICLE_TYPE={"小说","散文","诗歌","日记","记叙","议论","寓言","童话"};
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_write_article;
    }

    @Override
    protected void initView() {
        tvTitle.setTitle("写作中...");
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
        etTitle.setBackground(ColorTheme.getEditorDrawable());
        etContent.setBackground(ColorTheme.getEditorDrawable());
    }

    @Override
    protected void initData() {
        editTextListener();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(mContext
                ,R.layout.my_spinner_item
                ,ARTICLE_TYPE);
        articleType.setAdapter(arrayAdapter);
    }
    private void editTextListener(){
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (restNumber <= 3000) {
                    restNumber = s.length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvTextNum.setText(restNumber + "");
            }
        });
    }

    private void saveArticle(){
        if(etTitle.getText().toString().trim().length()<=0
                ||etContent.getText().toString().trim().length()<=0){
            WidgetUtils.showToast("标题和内容不能为空!");
            return;
        }else if(etContent.getText().toString().trim().length()<100){
            WidgetUtils.showToast("文章内容不能少于100字");
            return;
        }
        dlgLoading=new DlgLoading(mContext);
        Calendar c = Calendar.getInstance();
        final int mWay = c.get(Calendar.DAY_OF_WEEK);
        String articleTitle=etTitle.getText().toString().trim();
        String articleContent=etContent.getText().toString().trim();
        UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
        ArticleList articleEntity=new ArticleList();
        articleEntity.setUser(userInfo);
        articleEntity.setArticleTitle(articleTitle);
        articleEntity.setArticleType(articleType.getSelectedItem().toString());
        articleEntity.setArticleContent(articleContent);
        articleEntity.setCommentNum(0);
        articleEntity.setCollectNum(0);
        Log.d("tttt","mWay"+mWay);
        if(mWay!=1){
            articleEntity.setWeekDay(mWay-1);
        }else {
            articleEntity.setWeekDay(7);
        }
        dlgLoading.show("正在为您发布文章...");
        articleEntity.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                final DlgShowWrite dlgShowWrite = new DlgShowWrite(AtyWriteArticle.this);
                UserInfo userInfo = new UserInfo();
                userInfo.increment("vipNumber", 20);
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
                dlgLoading.dismiss();
                dlgShowWrite.show();
                dlgShowWrite.getTvUserName().setText(DataStorageUtils.getUserNickName() + "恭喜你成功发布了一篇文章");
                dlgShowWrite.getRlWriteAgain().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etContent.setText("");
                        etTitle.setText("");
                        dlgShowWrite.dismiss();
                    }
                });
                dlgShowWrite.getRlSeeOther().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, AtyAuthorArticle.class));
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                dlgLoading.dismiss();
                WidgetUtils.showToast("写作失败");
            }
        });
    }
}
