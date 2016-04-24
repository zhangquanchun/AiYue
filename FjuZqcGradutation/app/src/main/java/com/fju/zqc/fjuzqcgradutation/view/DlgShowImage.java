package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;
import com.nineoldandroids.animation.AnimatorSet;

/**
 * Created by zhang on 2016/3/18.
 */
public class DlgShowImage implements View.OnClickListener {
    private ImageView iv_max_image;
    private CustomDialog dialog = null;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private View dlgView;
    private AnimatorSet mAnimatorSet;
    public DlgShowImage(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dlgView=mInflater.inflate(R.layout.dlg_show_image,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        iv_max_image=(ImageView) dlgView.findViewById(R.id.iv_max_image);
        DlgAnimation.animatorFadeIn(dlgView).start();
    }
    @Override
    public void onClick(View v) {
        dialog.dismiss();
    }
    public void dismiss(){
        dialog.dismiss();
    }
    public void show(){
        dialog.show();
    }

    public ImageView getIv_max_image() {
        return iv_max_image;
    }

    public void setIv_max_image(ImageView iv_max_image) {
        this.iv_max_image = iv_max_image;
    }
}
