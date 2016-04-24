package com.fju.zqc.fjuzqcgradutation.bean;

/**
 * Created by zqc on 2015/9/9.
 */
public class ArticleListEntity {
    private String fid;
    private String title;
    private String content;
    private String viewNum;
    private String url;

    public ArticleListEntity(String fid,String title,String content,String viewNum){
        this.fid=fid;
        this.title=title;
        this.content=content;
        this.viewNum=viewNum;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
