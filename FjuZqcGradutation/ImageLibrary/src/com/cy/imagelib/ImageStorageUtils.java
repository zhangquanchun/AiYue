package com.cy.imagelib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Caiyuan Huang
 * <p>
 * 2015-5-29
 * </p>
 * <p>
 * 图片存储工具类
 * </p>
 */
public class ImageStorageUtils {

	/**
	 * 保存Bitmap到SD卡
	 * 
	 * @param bmp
	 * @param savePath
	 */
	public static void saveBitmapToSdCard(Bitmap bmp, String savePath) {
		if (bmp == null || TextUtils.isEmpty(savePath))
			return;
		byte[] bytes = ImageCoverUtils.bitmap2Bytes(bmp);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(savePath));
			out.write(bytes);
			out.flush();
		} catch (Exception e) {
			Log.d("tttt", "ERRORMAG========" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	/**
	 * 保存文件
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static String saveFile(Bitmap bm, String fileName) {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
		if(sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}

		String path =sdDir.getPath();
		File dirFile = new File(path);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + "/"+fileName);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myCaptureFile.getPath();
	}

	/**图片圆角*/
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;

		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		final RectF rectF = new RectF(rect);

		final float roundPx = pixels;

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}



}
