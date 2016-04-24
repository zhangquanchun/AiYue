package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.android.image.SmartImageView;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;

import java.util.List;


/**
 * Created by zqc on 2015/9/9.
 */
public class ArticleListAdapter extends AdapterBase<ArticleListEntity> {
    public ArticleListAdapter(Context context, List<ArticleListEntity> list) {
        super(context, list);
    }
    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent,
                               ArticleListEntity entity) {

        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.item_article_web, null);
        }
        entity=mList.get(position);
        if(entity.getFid()==null){
            ((RoundCImageView)getHolderView(convertView, R.id.fid)).setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance().loadImage(entity.getFid()
                    , ((RoundCImageView) getHolderView(convertView, R.id.fid)));
        }
        ((TextView)getHolderView(convertView,R.id.articleTitle)).setText(entity.getTitle());
        ((TextView)getHolderView(convertView,R.id.articleContent)).setText(entity.getContent());
        ((TextView)getHolderView(convertView,R.id.viewNum)).setText(entity.getViewNum());
        return convertView;
    }

}
