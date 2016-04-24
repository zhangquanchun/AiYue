package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.AuthorListEntity;

import java.util.List;

/**
 * Created by zhang on 2015/10/18.
 */
public class CollectListAdapter extends AdapterBase<AuthorListEntity> {
    public CollectListAdapter(Context context, List<AuthorListEntity> list) {
        super(context, list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, AuthorListEntity entity) {
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.item_article_list,null);
        }
        entity=mList.get(position);
        if(entity.getImageUrl()==null){
            ((RoundCImageView) getHolderView(convertView, R.id.fid)).setImageResource(R.drawable.ic_launcher);
        }else{
            ImageLoaderUtils.getInstance().loadImage(entity.getImageUrl()
                    , ((RoundCImageView) getHolderView(convertView, R.id.fid)));
        }
                ((TextView) getHolderView(convertView, R.id.articleTitle)).setText(entity.getArticleTitle());
        ((TextView) getHolderView(convertView,R.id.articleTime)).setVisibility(View.VISIBLE);
        ((TextView) getHolderView(convertView,R.id.articleTime)).setText(entity.getArticleTime());
        ((TextView)getHolderView(convertView,R.id.articleContent)).setText(entity.getContent());
        ((TextView)getHolderView(convertView,R.id.viewNum)).setText(entity.getAuthor());
        ((TextView)getHolderView(convertView,R.id.tvComment)).setText(entity.getCommentNum()+"");
        ((TextView)getHolderView(convertView,R.id.tvCollect)).setText(entity.getCollectNum()+"");
        return convertView;
    }
}
