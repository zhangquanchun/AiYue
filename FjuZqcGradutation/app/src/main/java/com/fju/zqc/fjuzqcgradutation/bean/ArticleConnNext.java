package com.fju.zqc.fjuzqcgradutation.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhang on 2015/12/22.
 */
public class ArticleConnNext extends BmobObject {
    private String articleContent;
    private Integer supportNum;
    private UserInfo user;
    private String articleTime;
    private ArticleConn articleConn;

    public ArticleConnNext() {

    }

    public ArticleConnNext(String articleContent, Integer supportNum, UserInfo user
            , String articleTime, ArticleConn articleConn) {
        this.articleContent = articleContent;
        this.supportNum = supportNum;
        this.user = user;
        this.articleTime = articleTime;
        this.articleConn=articleConn;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
    public Integer getSupportNum() {
        return supportNum;
    }

    public void setSupportNum(Integer supportNum) {
        this.supportNum = supportNum;
    }
    public String getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public ArticleConn getArticleConn() {
        return articleConn;
    }

    public void setArticleConn(ArticleConn articleConn) {
        this.articleConn = articleConn;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
