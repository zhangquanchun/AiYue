package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.cy.widgetlibrary.view.RoundCImageView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.nineoldandroids.animation.AnimatorSet;

/**
 * Created by ejianshen on 15/10/22.
 */
public class Share2WeiXin implements View.OnClickListener {
    private RoundCImageView ivImage;
    private View rlGoLogIn,rlNoLogIn,rlDisMiss;
    private CustomDialog dialog = null;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private TextView tvArticleTitle,tvAuthor;
    private View dlgView;
    private AnimatorSet mAnimatorSet;
    public Share2WeiXin(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dlgView=mInflater.inflate(R.layout.dlg_show_share, null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        ivImage=(RoundCImageView) dlgView.findViewById(R.id.ivImage);
        rlGoLogIn=(View) dlgView.findViewById(R.id.rlNoOpinion);
        rlNoLogIn=(View) dlgView.findViewById(R.id.rlIgnore);
        rlDisMiss=(View) dlgView.findViewById(R.id.rlDisMiss);
        tvArticleTitle=(TextView) dlgView.findViewById(R.id.tvArticleTitle);
        tvAuthor=(TextView) dlgView.findViewById(R.id.tvAuthor);
        DlgAnimation.animatorFall(dlgView).start();
        rlDisMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    @Override
    public void onClick(View v) {
        dialog.dismiss();
    }
    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
    public TextView getTvUserName(){
        return tvArticleTitle;
    }

    public TextView getTvAuthor() {
        return tvAuthor;
    }

    public RoundCImageView getIvImage() {
        return ivImage;
    }

    public CustomDialog getDialog() {
        return dialog;
    }
    public View getShare2WeiXin(){
        return rlGoLogIn;
    }
    public View getShare2Sina(){
        return rlNoLogIn;
    }
}
