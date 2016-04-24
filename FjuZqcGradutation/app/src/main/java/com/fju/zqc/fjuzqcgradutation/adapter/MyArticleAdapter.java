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
import com.fju.zqc.fjuzqcgradutation.bean.MyArticleListEntity;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;

import java.util.List;

/**
 * Created by ejianshen on 15/10/22.
 */
public class MyArticleAdapter extends AdapterBase<MyArticleListEntity> {
    public MyArticleAdapter(Context context, List<MyArticleListEntity> list) {
        super(context, list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, MyArticleListEntity entity) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_article_list,null);
        }
        entity=mList.get(position);
        if (entity.getImageUrl()==null){
            ((RoundCImageView) getHolderView(convertView, R.id.fid))
                    .setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance().loadImage(entity.getImageUrl()
                    , ((RoundCImageView) getHolderView(convertView, R.id.fid)));
        }
        ((TextView)getHolderView(convertView,R.id.articleTitle)).setText(entity.getArticleTitle());
        ((TextView)getHolderView(convertView,R.id.articleTime)).setVisibility(View.VISIBLE);
        ((TextView)getHolderView(convertView,R.id.articleTime)).setText(entity.getArticleTime());
        ((TextView)getHolderView(convertView,R.id.articleContent)).setText(entity.getArticleContent());
        ((TextView)getHolderView(convertView,R.id.viewNum)).setText("作者:"+entity.getArticleAuthor());
        ((TextView)getHolderView(convertView,R.id.tvComment)).setText(entity.getCommentNum()+"");
        ((TextView)getHolderView(convertView,R.id.tvCollect)).setText(entity.getCollectNum()+"");
        return convertView;
    }
}
