package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;

/**
 * Created by ejianshen on 15/10/22.
 */
public class DlgShowSaveOk implements View.OnClickListener {

    private ImageView ivImage;
    private View rlReadOn,rlBack,rlDlg;
    private CustomDialog dialog = null;
    private TextView tvUserName;
    public DlgShowSaveOk(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dlgView=mInflater.inflate(R.layout.dlg_show_save_ok,null);
        dialog=new CustomDialog(context).setContentView(dlgView, Gravity.CENTER).setCanceledOnTouchOutside(false);
        ivImage=(ImageView) dlgView.findViewById(R.id.ivImage);
        rlReadOn=(View) dlgView.findViewById(R.id.rlReadOn);
        rlBack=(View) dlgView.findViewById(R.id.rlBack);
        tvUserName=(TextView) dlgView.findViewById(R.id.tvUserName);
        rlDlg=(View) dlgView.findViewById(R.id.rlDlg);
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
    public View getRlReadOn(){
        return rlReadOn;
    }
    public View getRlBack(){
        return rlBack;
    }

}
