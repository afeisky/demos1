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
 * 圆角图片
 * Created by chaofei on 2018/02/06
 */
public class RoundRectImageDrawable extends Drawable {

    private RectF rectF;

    private Paint mPaint;

    private Bitmap bitmap;

    public RoundRectImageDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        //表示图片太大，则填充。TileMode还有repeat、mirror，表示重复
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
        //60表示圆角的半径
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

    //设置下面两个方法，保证原图可以完整的显示出来。
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
  //圆角Drawable
  imageView1.setImageDrawable(new RoundRectImageDrawable(b));
  //圆形Drawable
  imageView2.setImageDrawable(new CircleDrawable(b));
  */
}