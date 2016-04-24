package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by ejianshen on 15/11/6.
 */
public class CommentEntity {
    private String imageUrl;
    private String commentTime;
    private String author;
    private String commentContent;

    public CommentEntity(String imageUrl, String commentTime, String author, String commentContent) {
        this.imageUrl = imageUrl;
        this.commentTime = commentTime;
        this.author = author;
        this.commentContent = commentContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
