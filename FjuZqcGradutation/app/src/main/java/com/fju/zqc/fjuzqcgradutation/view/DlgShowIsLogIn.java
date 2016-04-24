package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.CustomCircleImageView;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by ejianshen on 15/10/22.
 */
public class DlgShowIsLogIn implements View.OnClickListener {
    private CustomCircleImageView ivImage;
    private View rlGoLogIn,rlNoLogIn;
    private CustomDialog dialog = null;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private TextView tvUserName;
    private View dlgView;
    private AnimatorSet mAnimatorSet;
    public DlgShowIsLogIn(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dlgView=mInflater.inflate(R.layout.dlg_show_log_in,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        ivImage=(CustomCircleImageView) dlgView.findViewById(R.id.ivImage);
        rlGoLogIn=(View) dlgView.findViewById(R.id.rlNoOpinion);
        rlNoLogIn=(View) dlgView.findViewById(R.id.rlIgnore);
        tvUserName=(TextView) dlgView.findViewById(R.id.tvUserName);
        DlgAnimation.animatorFall(dlgView).start();

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
        return tvUserName;
    }

    public CustomDialog getDialog() {
        return dialog;
    }
    public View getRlGoLogIn(){
        return rlGoLogIn;
    }
    public View getRlNoLogIn(){
        return rlNoLogIn;
    }
}
