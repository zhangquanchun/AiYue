package com.fju.zqc.fjuzqcgradutation.bean;

import java.io.Serializable;

/**
 * Created by ejianshen on 15/11/4.
 */
public class IntentToContentEntity implements Serializable {
    public String title;
    public String articleId;
    public String content;
    public String author;
    public String imageUrl;
    public boolean isCollect;
    public String pid;
    public ArticleList articleList;
    public final static String INTENT_KEY="atyAuthorContent";

    public IntentToContentEntity(String title, String articleId, String content
            , String author, String imageUrl, boolean isCollect,String pid
            ,ArticleList articleList) {
        this.title = title;
        this.articleId = articleId;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.isCollect = isCollect;
        this.pid=pid;
        this.articleList=articleList;
    }

}
