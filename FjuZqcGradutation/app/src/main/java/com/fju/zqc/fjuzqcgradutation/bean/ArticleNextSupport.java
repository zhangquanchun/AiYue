package com.fju.zqc.fjuzqcgradutation.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhang on 2015/12/30.
 */
public class ArticleNextSupport extends BmobObject {
    private String articleConnId;
    private String userName;

    public ArticleNextSupport() {
    }

    public ArticleNextSupport(String articleConnId, String userName) {
        this.articleConnId = articleConnId;
        this.userName = userName;
    }

    public String getArticleConnId() {
        return articleConnId;
    }

    public void setArticleConnId(String articleConnId) {
        this.articleConnId = articleConnId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
