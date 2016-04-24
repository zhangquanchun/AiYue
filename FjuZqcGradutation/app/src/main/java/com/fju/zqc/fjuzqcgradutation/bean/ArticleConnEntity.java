package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by zhang on 2015/12/22.
 */
public class ArticleConnEntity {
    private String articleTitle;
    private String articleContent;
    private String authorName;
    private Integer isWriting;
    private String articleTime;
    private String articleId;
    private String imageUrl;
    private Integer addPersonNum;
    private ArticleConn articleConn;
    private UserInfo userInfo;

    public ArticleConnEntity(String articleTitle, String articleContent
            , String authorName, Integer isWriting, String articleTime
            , String articleId, String imageUrl, Integer addPersonNum) {
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.authorName = authorName;
        this.isWriting = isWriting;
        this.articleTime = articleTime;
        this.articleId = articleId;
        this.imageUrl = imageUrl;
        this.addPersonNum = addPersonNum;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getIsWriting() {
        return isWriting;
    }

    public void setIsWriting(Integer isWriting) {
        this.isWriting = isWriting;
    }

    public String getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAddPersonNum() {
        return addPersonNum;
    }

    public void setAddPersonNum(Integer addPersonNum) {
        this.addPersonNum = addPersonNum;
    }

    public ArticleConn getArticleConn() {
        return articleConn;
    }

    public void setArticleConn(ArticleConn articleConn) {
        this.articleConn = articleConn;
    }
}
