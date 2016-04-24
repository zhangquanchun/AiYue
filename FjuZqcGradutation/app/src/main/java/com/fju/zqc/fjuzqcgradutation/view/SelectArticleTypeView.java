package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.nineoldandroids.animation.AnimatorSet;

/**
 * Created by ejianshen on 15/10/22.
 */
public class SelectArticleTypeView  {

    private ListView listView;
    private CustomDialog dialog;
    public SelectArticleTypeView(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=mInflater.inflate(R.layout.select_article_type,null);
        dialog=new CustomDialog(context).setContentView(view).setCanceledOnTouchOutside(true);
        listView=(ListView)view.findViewById(R.id.listView);
        DlgAnimation.animatorSlit(view).start();
    }
    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
    public ListView getListView() {
        return listView;
    }
}
