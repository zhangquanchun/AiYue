package com.fju.zqc.fjuzqcgradutation.jsoup;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.fju.zqc.fjuzqcgradutation.activity.AtyArticleView;
import com.fju.zqc.fjuzqcgradutation.adapter.ArticleListAdapter;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.net.GetWebArticle;
import com.fju.zqc.fjuzqcgradutation.utils.AddListViewMore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqc on 2015/9/9.
 */
public class GetCoArticleList extends GetArticleListBase {
    private Context context;
    private ArticleListEntity entity;
    private static List<ArticleListEntity> mEntities=new ArrayList<ArticleListEntity>();
    private static ArticleListAdapter adapter;
    private AddListViewMore listView;
    private Handler handler;
    private static String currentUrl;
    private static List<String> title=new ArrayList<String>();
    private static List<String> htmlContents=new ArrayList<String>();
    private static List<String> viewNum=new ArrayList<String>();
    private static List<String> nextUrl=new ArrayList<String>();
    private static List<String> imageUrlList=new ArrayList<String>();
    private String homeUrl;
    private static List<String> detailUrl=new ArrayList<String>();
    private int currentPage=0;
    public GetCoArticleList(Context context, Handler handler, AddListViewMore listView){

        this.listView=listView;
        this.context=context;
        this.handler=handler;
    }
    public void getHtmlContent(String content){

        try{
            Document doc= Jsoup.parse(content);
            Elements titles=doc.getElementsByTag("div").select("div[class$=cbody]").select("a");
            Elements contents=doc.getElementsByTag("li").select("p[class$=intro]");
            Elements views=doc.getElementsByTag("li").select("span[class$=info]");
            Elements htmlUrl=doc.getElementsByTag("select").select("select[name$=sldd]").select("option");
            Elements currentHtml=doc.getElementsByTag("div").select("div[class$=place]").select("a");
            Elements imageUrl=doc.getElementsByTag("a").select("a[class$=preview]").select("img");
            Element element;
            /**
             * title
             */
            for(int i=0;i<titles.size();i++){

                element=titles.get(i);
                title.add(element.text());
                detailUrl.add(element.attr("href"));
                Log.d("Love", "====Love"+element.text());
            }
            /**
             * content
             */
            for(int i=0;i<contents.size();i++){

                element=contents.get(i);
                htmlContents.add(element.text());
            }
            /**
             * view and author
             */
            for(int i=0;i<views.size();i++) {

                element = views.get(i);
                viewNum.add(element.text());
            }
            /**
             * url
             */
            for(int i=0;i<htmlUrl.size();i++){

                element=htmlUrl.get(i);
                nextUrl.add(element.attr("value"));
                Log.d("xxxx","======="+element.attr("value"));
            }
            /**
             * this page url
             */
            currentUrl=null;
            for(int i=0;i<2;i++){
                element=currentHtml.get(i);
                if(currentUrl==null){
                    currentUrl=element.attr("href");
                    homeUrl=element.attr("href");
                }else{
                    currentUrl=currentUrl+element.attr("href").substring(1);
                }
            }
            /**
             * image url
             */
            for(int i=0;i<imageUrl.size();i++){
                element=imageUrl.get(i);
                String url=homeUrl+element.attr("src").substring(1);
                imageUrlList.add(url);
                Log.d("mxxxx",url);
            }

               setHtmlData();

        }catch (Exception e){
            Log.d("qqqq", "error" + content);
        }
    }

    public void getNextHtmlData(int i){

        if(i<=nextUrl.size()){
            String content= GetWebArticle.getWebArticle(currentUrl + nextUrl.get(i));
            Log.d("Next", "****************URL" + currentUrl + nextUrl.get(i) + "\n" + content);
            getHtmlContent(content);
        }
    }
   public void setHtmlData(){

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = currentPage; i < title.size(); i++) {

                    entity = new ArticleListEntity(imageUrlList.get(i), title.get(i), htmlContents.get(i), viewNum.get(i));
                    entity.setUrl(homeUrl + detailUrl.get(i).substring(1));
                    mEntities.add(entity);
                }
                currentPage = title.size();
                if (adapter == null) {
                    adapter = new ArticleListAdapter(context, mEntities);
                    listView.setAdapter(adapter);
                } else {
                    Log.d("dddd", "=======position******" );
                    adapter.notifyDataSetChanged();
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(context, AtyArticleView.class);
                        intent.putExtra("url", mEntities.get(i).getUrl());
                        intent.putExtra("title",mEntities.get(i).getTitle());
                        intent.putExtra("author",mEntities.get(i).getViewNum());
                        intent.putExtra("content",mEntities.get(i).getContent());
                        intent.putExtra("imageUrl",mEntities.get(i).getFid());
                        context.startActivity(intent);
                    }
                });

            }
        });
    }
}
