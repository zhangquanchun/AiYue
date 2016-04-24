package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.activity.AtyUserInfo;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleConnEntity;
import com.fju.zqc.fjuzqcgradutation.bean.AuthorListEntity;

import java.util.List;

/**
 * Created by zhang on 2015/10/18.
 */
public class ArticleConnAdapter extends AdapterBase<ArticleConnEntity> {
    public ArticleConnAdapter(Context context, List<ArticleConnEntity> list) {
        super(context, list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, ArticleConnEntity entity1) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_article_conn,null);
        }
        final ArticleConnEntity entity=mList.get(position);
        if(entity.getImageUrl()==null){
            ((RoundCImageView) getHolderView(convertView, R.id.fid)).setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance().loadImage(entity.getImageUrl()
                    , ((RoundCImageView) getHolderView(convertView, R.id.fid)));
        }
                ((TextView) getHolderView(convertView, R.id.articleTitle)).setText(entity.getArticleTitle());
        ((TextView) getHolderView(convertView,R.id.articleTime)).setVisibility(View.VISIBLE);
        ((TextView) getHolderView(convertView,R.id.articleTime)).setText(entity.getArticleTime());
        ((TextView)getHolderView(convertView,R.id.articleContent)).setText(entity.getArticleContent());
        ((TextView)getHolderView(convertView,R.id.viewNum)).setText(entity.getAuthorName());
        ((TextView)getHolderView(convertView,R.id.tvAddNum)).setText(entity.getAddPersonNum()+"");
        ((RoundCImageView) getHolderView(convertView, R.id.fid)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AtyUserInfo.class);
                intent.putExtra("pid",entity.getUserInfo().getObjectId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
