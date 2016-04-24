package com.fju.zqc.fjuzqcgradutation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cy.widgetlibrary.base.BaseFragment;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.jsoup.GetArticleList;
import com.fju.zqc.fjuzqcgradutation.net.GetWebArticle;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ejianshen on 15/9/11.
 */
public class FragmentAllArticle extends BaseFragment implements
        AddListViewMore.ILoadListener {
    private String content;
    private Handler handler=new Handler();
    private GetArticleList getArticleList;
    @BindView
    private AddListViewMore listView;
    private DlgLoading dlgLoading;
    private int currentPageNum=1;
    private Toast mToast;
    @BindView
    private View rlStudent,rlLove,rlClassic,rlAffection,rlCourage;
    private CustomTitleView title;
    private final static int ARTICLE_POSITION=0;
    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_all_article;
    }

    @Override
    protected void initView(View contentView, LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        dlgLoading=new DlgLoading(getActivity());
        getWebArticle("http://www.lookmw.cn/xuesheng/");
        Log.d("dddd", "11111111");
    }

    public void getWebArticle(final String url) {
       final DlgLoading dlgLoading=new DlgLoading(getActivity());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dlgLoading.show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getArticleList=new GetArticleList(getActivity(),handler,listView);
                content= GetWebArticle.getWebArticle(url);
                Log.d("dddd","===="+content);
                getArticleList.getHtmlContent(content);
                listView.setInterface(FragmentAllArticle.this);
                dlgLoading.dismiss();
            }
        }).start();

    }
    private void getArticleDetail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               // getArticleList.getNextHtmlData(currentPageNum);
                currentPageNum++;
            }
        }).start();
    }

            @Override
            public void onLoad() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getArticleDetail();
                        listView.LoadComplate();
                    }
                }, 2000);
            }
}
