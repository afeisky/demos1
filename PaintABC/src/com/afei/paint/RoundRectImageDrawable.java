package com.afei.paint;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.Shader;
/**
 * Բ��ͼƬ
 * Created by chaofei on 2018/02/06
 */
public class RoundRectImageDrawable extends Drawable {

    private RectF rectF;

    private Paint mPaint;

    private Bitmap bitmap;

    public RoundRectImageDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        //��ʾͼƬ̫������䡣TileMode����repeat��mirror����ʾ�ظ�
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        //60��ʾԲ�ǵİ뾶
        canvas.drawRoundRect(rectF, 60, 60, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    //��������������������֤ԭͼ������������ʾ������
    @Override
    public int getIntrinsicWidth() {
        return bitmap.getWidth();
    }
    @Override
    public int getIntrinsicHeight() {
        return bitmap.getHeight();
    }
    
    /* call():
    Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.cute);
  //Բ��Drawable
  imageView1.setImageDrawable(new RoundRectImageDrawable(b));
  //Բ��Drawable
  imageView2.setImageDrawable(new CircleDrawable(b));
  */
}