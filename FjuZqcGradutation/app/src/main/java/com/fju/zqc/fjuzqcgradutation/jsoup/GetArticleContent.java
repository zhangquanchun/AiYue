package com.fju.zqc.fjuzqcgradutation.jsoup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cy.widgetlibrary.WidgetUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.content.DlgLoading;
import com.cy.widgetlibrary.content.DlgTextMsg;
import com.fju.zqc.fjuzqcgradutation.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqc on 2015/9/9.
 */
public class GetArticleContent {
    private Context context;
    private Handler handler;
    private StringBuffer mTitle=new StringBuffer(),title=new StringBuffer();
    private TextView articleTitle;
    private ListView mArticle;
    private DlgLoading dlgLoading;
    private String htmlContent;
    private ArticleAdapter adapter=null;
    private List<String> articleContent=new ArrayList<>();
    private TextView totalTxtSize;
    private Document doc;
    private boolean isShow=false;
    private View rlBack,llBack;
    private ImageView imageView;

    public GetArticleContent(final Context context,Handler handler
            ,TextView articleTitle
            ,ListView mArticle,TextView totalTxtSize,View rlBack,View llBack){
        this.articleTitle=articleTitle;
        this.mArticle=mArticle;
        this.context=context;
        this.handler=handler;
        this.totalTxtSize=totalTxtSize;
        this.rlBack=rlBack;
        this.llBack=llBack;
        /**dialog show*/
        handler.post(new Runnable() {
            @Override
            public void run() {
                dlgLoading = new DlgLoading(context);
                dlgLoading.show();
            }
        });
    }
    public void getDetailArticle(String content) throws Exception{


        doc= Jsoup.parse(content);
        String html=doc.html().replace("<br />", "#");
        doc=Jsoup.parse(html);
         title.append(doc.getElementsByClass("title").text());
         htmlContent=doc.getElementsByClass("content").text();
         for(int i=0;i<htmlContent.length();i++){
             char item=htmlContent.charAt(i);
             if(String.valueOf(item).equals("#")){
                 String list="      "+mTitle.toString().trim();
                 if(mTitle.toString().trim().length()>1){
                     articleContent.add(list);
                 }
                 mTitle=new StringBuffer();
             }else{
                 mTitle.append(item);
             }
         }
        mArticle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String text=articleContent.get(position);
                DlgTextMsg textMsg=new DlgTextMsg(context, new DlgTextMsg.ConfirmDialogListener() {
                    @Override
                    public void onOk(DlgTextMsg dlg) {
                        ClipboardManager clipboardManager=(ClipboardManager)
                                context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setText(text);
                        WidgetUtils.showToast("复制成功");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                textMsg.setBtnString("是","否");
                textMsg.show("是否需要复制该段落？");
                return true;
            }
        });
        mArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShow){
                    rlBack.setVisibility(View.GONE);
                    isShow=false;
                }else{
                    rlBack.setVisibility(View.VISIBLE);
                    isShow=true;
                }
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(adapter==null){
                    adapter=new ArticleAdapter(context,articleContent);
                    mArticle.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }
        });
        /**dialog dismiss*/
        handler.post(new Runnable() {
            @Override
            public void run() {
                dlgLoading.dismiss();
            }
        });
        setData();
    }
    private void setData(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                totalTxtSize.setText("字数:"+htmlContent.length());
                articleTitle.setText(title);
            }
        });
    }
    class ArticleAdapter extends AdapterBase<String> {

        ArticleAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        protected View getItemView(int position, View convertView, ViewGroup parent, String entity) {
            if(convertView==null)
            convertView=mInflater.inflate(R.layout.item_article_content,null);
            entity=mList.get(position);
            ((TextView)(getHolderView(convertView, R.id.tvContent))).setText(entity);
            return convertView;
        }
    }

}
