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
public class DlgIsDelete implements View.OnClickListener {
    private View rl_delete,rl_save;
    private CustomDialog dialog = null;
    private static final int DURATION = 1 * 700;
    protected long mDuration =DURATION ;
    private View dlgView;
    private AnimatorSet mAnimatorSet;
    public DlgIsDelete(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dlgView=mInflater.inflate(R.layout.dlg_is_delete,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        rl_delete=(View) dlgView.findViewById(R.id.rl_delete);
        rl_save=(View) dlgView.findViewById(R.id.rl_save);
        DlgAnimation.animatorFall(dlgView).start();
        rl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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

    public View getRl_delete() {
        return rl_delete;
    }

}
