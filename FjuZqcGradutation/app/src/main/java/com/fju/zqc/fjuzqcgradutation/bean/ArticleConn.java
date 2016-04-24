package com.fju.zqc.fjuzqcgradutation.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhang on 2015/12/22.
 */
public class ArticleConn extends BmobObject {
    private String articleTitle;
    private String articleContent;
    private Integer isWriting;
    private UserInfo user;
    private Integer addPersonNum;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public Integer getIsWriting() {
        return isWriting;
    }

    public void setIsWriting(Integer isWriting) {
        this.isWriting = isWriting;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Integer getAddPersonNum() {
        return addPersonNum;
    }

    public void setAddPersonNum(Integer addPersonNum) {
        this.addPersonNum = addPersonNum;
    }
}
