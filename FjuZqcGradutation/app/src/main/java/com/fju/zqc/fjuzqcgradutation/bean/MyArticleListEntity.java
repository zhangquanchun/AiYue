package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by ejianshen on 15/10/22.
 */
public class MyArticleListEntity{
    private String objectId;
    private String articleTitle;
    private String articleContent;
    private String articleTime;
    private String articleAuthor;
    private Integer commentNum;
    private Integer collectNum;
    private ArticleList articleList;
    private String imageUrl;

    public MyArticleListEntity(String objectId,String articleTitle, String articleContent, String articleTime, String articleAuthor) {
        this.objectId=objectId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleTime = articleTime;
        this.articleAuthor = articleAuthor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(Integer collectNum) {
        this.collectNum = collectNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public String getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public ArticleList getArticleList() {
        return articleList;
    }

    public void setArticleList(ArticleList articleList) {
        this.articleList = articleList;
    }
}
