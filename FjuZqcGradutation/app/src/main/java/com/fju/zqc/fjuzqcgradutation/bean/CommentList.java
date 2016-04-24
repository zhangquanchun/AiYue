package com.fju.zqc.fjuzqcgradutation.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ejianshen on 15/11/6.
 */
public class CommentList extends BmobObject{

    private String commentMsg;
    private ArticleList articleList;
    private UserInfo user;



    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    public ArticleList getArticleList() {
        return articleList;
    }

    public void setArticleList(ArticleList articleList) {
        this.articleList = articleList;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
