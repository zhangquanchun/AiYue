package com.fju.zqc.fjuzqcgradutation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cy.widgetlibrary.base.BaseFragment;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.DlgLoading;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.jsoup.GetSuiBiArticleList;
import com.fju.zqc.fjuzqcgradutation.net.GetWebArticle;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;
import com.hhtech.utils.NetIsAvailable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ejianshen on 15/10/9.
 */
public class FragmentSuiBiArticle extends BaseFragment implements AddListViewMore.ILoadListener {
    private String content;
    private Handler handler=new Handler();
    private GetSuiBiArticleList getArticleList;
    @BindView
    private AddListViewMore listView;
    private DlgLoading dlgLoading;
    private int currentPageNum=1;
    @BindView
    private TextView tvReLoad;
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_web_article;
    }

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected void initView(View contentView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //dlgLoading=new DlgLoading(getActivity());
        getWebArticle("http://www.lookmw.cn/suibi/");
        if(!NetIsAvailable.isNetworkConnected(getActivity())){
            tvReLoad.setVisibility(View.VISIBLE);
        }
        tvReLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWebArticle("http://www.lookmw.cn/suibi/");
                tvReLoad.setVisibility(View.GONE);
            }
        });
    }
    public void getWebArticle(final String url){
        //final DlgLoading dlgLoading=new DlgLoading(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //GetArticleList.ResetListEntity();
                getArticleList=new GetSuiBiArticleList(getActivity(),handler,listView);
                content= GetWebArticle.getWebArticle(url);
                Log.d("dddd", "====" + content);
                getArticleList.getHtmlContent(content);
                listView.setInterface(FragmentSuiBiArticle.this);
            }
        }).start();


    }
    private void getArticleDetail(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getArticleList.getNextHtmlData(currentPageNum);
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
