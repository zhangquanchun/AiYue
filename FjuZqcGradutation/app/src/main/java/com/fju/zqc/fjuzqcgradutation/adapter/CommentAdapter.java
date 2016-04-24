package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.imagelib.ImageLoaderUtils;
import com.cy.widgetlibrary.base.AdapterBase;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.CommentEntity;

import java.util.List;

/**
 * Created by ejianshen on 15/11/6.
 */
public class CommentAdapter extends AdapterBase<CommentEntity> {
    public CommentAdapter(Context context, List<CommentEntity> list) {
        super(context, list);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent, CommentEntity entity) {
        entity=mList.get(position);
        if(convertView==null)
            convertView=mInflater.inflate(R.layout.item_comment_list,null);
        ImageLoaderUtils.getInstance().loadImage(entity.getImageUrl(),((RoundCImageView) getHolderView(convertView, R.id.ivImageView)));
        ((TextView)getHolderView(convertView,R.id.tvAuthor)).setText(entity.getAuthor());
        ((TextView)getHolderView(convertView,R.id.tvContent)).setText(entity.getCommentContent());
        ((TextView)getHolderView(convertView,R.id.tvTime)).setText(entity.getCommentTime());

        return convertView;
    }
}
