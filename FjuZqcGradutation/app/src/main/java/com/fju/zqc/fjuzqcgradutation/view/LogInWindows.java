package com.fju.zqc.fjuzqcgradutation.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fju.zqc.fjuzqcgradutation.R;

/**
 * Created by ejianshen on 15/7/30.
 */
public class LogInWindows extends PopupWindow {
    private Button btnSina;
    private Button btnWeiXin;
    private Button btnQQ;
    private Button btnCancle;
    private View mMenuView;

    public LogInWindows(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.log_in, null);
        btnSina = (Button) mMenuView.findViewById(R.id.SinaWeiBoLogIn);
        btnWeiXin = (Button) mMenuView.findViewById(R.id.WeiXinLogIn);
        btnQQ = (Button) mMenuView.findViewById(R.id.AReadLogIn);
        btnCancle=(Button) mMenuView.findViewById(R.id.btn_cancel);
//        //取消按钮
//        btnCancle.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                //销毁弹出框
//                dismiss();
//            }
//        });
        //设置按钮监听
        btnCancle.setOnClickListener(itemsOnClick);
        btnSina.setOnClickListener(itemsOnClick);
        btnWeiXin.setOnClickListener(itemsOnClick);
        btnQQ.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(GridLayout.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popStyle);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
    public Button getBtnSina() {
        return btnSina;
    }

    public Button getBtnWeiXin() {
        return btnWeiXin;
    }

    public Button getBtnQQ() {
        return btnQQ;
    }

    public Button getBtnCancle() {
        return btnCancle;
    }

    public View getmMenuView() {
        return mMenuView;
    }

}


