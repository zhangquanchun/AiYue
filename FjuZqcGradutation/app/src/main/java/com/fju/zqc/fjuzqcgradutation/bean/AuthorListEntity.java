package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by zhang on 2015/10/18.
 */
public class AuthorListEntity {
    private String imageUrl;
    private String articleTitle;
    private String content;
    private String author;
    private String articleTime;
    private String articleId;
    private String objectId;
    private int commentNum;
    private int collectNum;
    private String pid;
    private String type;
    private String articleUrl;
    private ArticleList articleList;
    private UserInfo userInfo;

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthorListEntity() {
    }

    public AuthorListEntity(String imageUrl, String articleTitle, String content,String articleTime, String author) {
        this.imageUrl = imageUrl;
        this.articleTitle = articleTitle;
        this.content = content;
        this.author = author;
        this.articleTime=articleTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticleTime() {
        return articleTime;
    }

    public ArticleList getArticleList() {
        return articleList;
    }

    public void setArticleList(ArticleList articleList) {
        this.articleList = articleList;
    }
}
