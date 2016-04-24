package com.fju.zqc.fjuzqcgradutation.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Caiyuan Huang
 * <p>
 * 2015-7-1
 * </p>
 * <p>
 * 字符串工具类
 * </p>
 */
public class StringUtils {
	/**
	 * 创建｛文字内容、字体颜色、字体大小｝分段文字集合体
	 * 
	 * @param text
	 * @param color
	 * @param textSize
	 * @return
	 */
	public static SpannableStringBuilder creSpanString(String[] text,
			int[] color, int[] textSize) {
		if (text == null || color == null || textSize == null)
			throw new IllegalArgumentException("参数不能为空");
		if (text.length != color.length || text.length != textSize.length)
			throw new IllegalArgumentException("参数数组长度不一致");
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			for (int i = 0; i < text.length; i++) {
				SpannableString sp = new SpannableString(text[i]);
				sp.setSpan(new ForegroundColorSpan(color[i]), 0, sp.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				sp.setSpan(new AbsoluteSizeSpan(textSize[i], true), 0,
						sp.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				sb.append(sp);
			}
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb;
	}
}
