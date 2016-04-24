package com.fju.zqc.fjuzqcgradutation.jsoup;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.fju.zqc.fjuzqcgradutation.activity.AtyArticleView;
import com.fju.zqc.fjuzqcgradutation.adapter.ArticleListAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.net.GetWebArticle;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqc on 2015/9/9.
 */
public abstract class GetArticleListBase extends Thread {
    public abstract void getHtmlContent(String content);
    public abstract void getNextHtmlData(int i);
}
