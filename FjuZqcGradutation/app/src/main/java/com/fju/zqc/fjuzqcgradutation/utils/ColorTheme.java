package com.fju.zqc.fjuzqcgradutation.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.cy.widgetlibrary.utils.BgDrawableUtils;
import com.fju.zqc.fjuzqcgradutation.R;
import com.hhtech.base.AppUtils;

/**
 * Created by mingchaocai on 15/8/6.
 */
public class ColorTheme {

	public static StateListDrawable getEditorDrawable() {
		return creEditorBorder(4, 1);
	}


	/**
	 *蓝色背景button，注册按钮背景
	 */
	public static StateListDrawable getBlueBgBtn() {
		StateListDrawable btnCancelBg = BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(getColor(R.color.btn_blue_normal), 5, 1, 0x00000000, null),
				BgDrawableUtils.creShape(getColor(R.color.btn_blue_pressed), 5, 1, 0x00000000, null));
		return btnCancelBg;
	}


	/**
	 * 黄色背景button，我已注册按钮的背景
	 * @return
	 */
	public static StateListDrawable getYellowBgBtn() {
		StateListDrawable btnCancelBg = BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xfff6b70c, 5, 1, 0x00000000, null),
				BgDrawableUtils.creShape(0xffee9200, 5, 1, 0x00000000, null));
		return btnCancelBg;
	}


	/**
	 *淡蓝色背景button， 打开蓝牙按钮的背景
	 * @return
	 */
	public static StateListDrawable getNatBlueBgBtn(){
		StateListDrawable btnCancelBg= BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xffe9fcff, 5, 1, 0x00000000, null),
				BgDrawableUtils.creShape(0xffb6ebf3, 5, 1, 0x00000000, null));
		return btnCancelBg;
	}

	/**
	 *灰色－蓝色背景button， 时间、距离、卡路里的背景
	 * @return
	 */
	public static StateListDrawable getBlueAndGrayBgBtn(){
		StateListDrawable btnCancelBg= BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xfff6f6f6, 5, 1, 0xff747474, null),
				BgDrawableUtils.creShape(0xffe9fcff, 5, 1, 0xff47bfd1, null));
		return btnCancelBg;
	}

	/**
	 *天蓝色背景button， 获取验证码按钮的背景
	 * @return
	 */
	public static StateListDrawable getSkyBlueBgBtn(){
		StateListDrawable btnCancelBg= BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xff05bbe2, 5, 1, 0x00000000, null),
				BgDrawableUtils.creShape(0xff0999b8, 5, 1, 0x00000000, null));
		return btnCancelBg;
	}


	public static Drawable getTitleBarIndicator(){
		return	BgDrawableUtils.creShape(0xff05bbe2, 5, 1, 0x00000000, null);
	}

	public static int getColor(int id) {
		return AppUtils.getResource().getColor(id);
	}

	public static StateListDrawable creEditorBorder(int roundRaidus,
			int strokeWidth) {
		GradientDrawable normal = BgDrawableUtils.creShape(0x00000000,
				roundRaidus, strokeWidth,
				getColor(R.color.editor_border_normal), null);
		GradientDrawable foucus = BgDrawableUtils.creShape(0x00000000,
				roundRaidus, strokeWidth,
				getColor(R.color.editor_border_focused), null);
		GradientDrawable errState = BgDrawableUtils.creShape(0x00000000,
				roundRaidus, strokeWidth,
				getColor(R.color.editor_border_error_hint), null);
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[] { android.R.attr.state_selected }, errState);
		bg.addState(new int[] { android.R.attr.state_pressed }, foucus);
		bg.addState(new int[] { android.R.attr.state_focused }, foucus);
		bg.addState(new int[] { 0 }, normal);
		return bg;
	}

	public static GradientDrawable creWhiteShape() {
		return BgDrawableUtils.creShape(0xffffffff, 5);
	}

	/**
	 * 获取对话框title部分的背景
	 * 
	 * @return
	 */
	public static GradientDrawable getDlgTitleShape() {
		GradientDrawable bgTitle = BgDrawableUtils.creShape(0xff05bce1,
				new float[]{5, 5, 5, 5, 0, 0, 0, 0});
		return bgTitle;
	}

	/**
	 * 获取对话框取消按钮的背景
	 * 
	 * @return
	 */
	public static StateListDrawable getDlgCancelBtnSelector() {
		StateListDrawable btnCancelBg = BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xfff6f6f6, 5, 1, 0xff747474, null),
				BgDrawableUtils.creShape(0xffededed, 5, 1, 0xff747474, null));
		return btnCancelBg;
	}

	/**
	 * 获取对话框确认按钮的背景
	 * 
	 * @return
	 */
	public static StateListDrawable getDlgEnsureBtnSelector() {
		StateListDrawable btnEnsureBg = BgDrawableUtils.crePressSelector(
				BgDrawableUtils.creShape(0xffe8feff, 5, 1, 0xff46c1d0, null),
				BgDrawableUtils.creShape(0xffd1f9fb, 5, 1, 0xff46c1d0, null));
		return btnEnsureBg;
	}
}
