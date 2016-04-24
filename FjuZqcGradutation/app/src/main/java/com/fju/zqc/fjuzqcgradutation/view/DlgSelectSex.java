package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;


import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;
import com.nineoldandroids.animation.AnimatorSet;

/**
 * Created by zhang on 2016/3/18.
 */
public class DlgSelectSex implements View.OnClickListener {
    private View rl_man,rl_female;
    private CustomDialog dialog = null;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private View dlgView;
    private AnimatorSet mAnimatorSet;
    public DlgSelectSex(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dlgView=mInflater.inflate(R.layout.dlg_select_sex,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        rl_man=(View) dlgView.findViewById(R.id.rl_man);
        rl_female=(View) dlgView.findViewById(R.id.rl_female);
        DlgAnimation.animatorFall(dlgView).start();
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

    public View getRl_man() {
        return rl_man;
    }

    public View getRl_female() {
        return rl_female;
    }
}
