package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;

/**
 * Created by ejianshen on 15/10/22.
 */
public class DlgShowWrite implements View.OnClickListener {

    private ImageView ivImage;
    private View rlWriteAgain,rlSeeOther,rlDlg;
    private CustomDialog dialog = null;
    private TextView tvUserName,tvSeeOther,tvDetail,tvWriteOther;
    public DlgShowWrite(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dlgView=mInflater.inflate(R.layout.dlg_show_write,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        ivImage=(ImageView) dlgView.findViewById(R.id.ivImage);
        rlWriteAgain=(View) dlgView.findViewById(R.id.rlNoOpinion);
        rlSeeOther=(View) dlgView.findViewById(R.id.rlIgnore);
        tvUserName=(TextView) dlgView.findViewById(R.id.tvUserName);
        tvSeeOther=(TextView) dlgView.findViewById(R.id.tvSeeOther);
        tvDetail=(TextView) dlgView.findViewById(R.id.tvDetail);
        rlDlg=(View) dlgView.findViewById(R.id.rlDlg);
        tvWriteOther=(TextView) dlgView.findViewById(R.id.tvWriteOther);
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

    public ImageView getIvImage() {
        return ivImage;
    }

    public void setIvImage(ImageView ivImage) {
        this.ivImage = ivImage;
    }

    public CustomDialog getDialog() {
        return dialog;
    }
    public View getRlWriteAgain(){
        return rlWriteAgain;
    }
    public View getRlSeeOther(){
        return rlSeeOther;
    }

    public TextView getTvSeeOther() {
        return tvSeeOther;
    }

    public TextView getTvDetail() {
        return tvDetail;
    }

    public TextView getTvWriteOther() {
        return tvWriteOther;
    }
}
