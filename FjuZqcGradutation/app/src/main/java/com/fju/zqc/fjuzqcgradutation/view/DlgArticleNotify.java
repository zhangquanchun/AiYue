package com.fju.zqc.fjuzqcgradutation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cy.widgetlibrary.content.CustomDialog;
import com.cy.widgetlibrary.view.DlgAnimation;
import com.fju.zqc.fjuzqcgradutation.R;

/**
 * Created by ejianshen on 15/10/22.
 */
public class DlgArticleNotify {

    private ImageButton ibtOk;
    private CheckBox ibtNoShowAgain;
    private CustomDialog dialog;
    public DlgArticleNotify(Context context){
        LayoutInflater mInflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=mInflater.inflate(R.layout.dlg_article_notify,null);
        dialog=new CustomDialog(context).setContentView(view).setCanceledOnTouchOutside(true);
        ibtNoShowAgain=(CheckBox)view.findViewById(R.id.ibtNoShowAgain);
        ibtOk=(ImageButton) view.findViewById(R.id.ibtOk);
        DlgAnimation.animatorSlit(view).start();
    }
    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public ImageButton getIbtOk() {
        return ibtOk;
    }

    public CheckBox getIbtNoShowAgain() {
        return ibtNoShowAgain;
    }
}
