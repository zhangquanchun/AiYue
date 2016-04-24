package com.cy.widgetlibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by ejianshen on 15/10/31.
 */
public class RoundCImageView extends ImageView {
    private Paint paint;
    public RoundCImageView(Context context) {
        super(context);
        paint=new Paint();
    }

    public RoundCImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
    }

    public RoundCImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
    }

    public RoundCImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable=getDrawable();
        if(drawable!=null){
            Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
            Bitmap b=getRoundBitmap(bitmap, 20);
            final Rect rectSrc=new Rect(0,0,b.getWidth(),b.getHeight());
            final Rect rectDest=new Rect(0,0,getWidth(),getHeight());
            paint.reset();
            canvas.drawBitmap(b,rectSrc,rectDest,paint);
        }else {
            super.onDraw(canvas);
        }

    }
    /**
     * 获取圆角矩形图片方法
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author zqc
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;


    }
}
