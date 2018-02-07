package com.afei.paint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chaofei on 2018/02/06
 */
	public class PaintBoard extends View {
		private String TAG="PaintBoard";

		private Paint mPaint = null;
		private Bitmap mBitmap = null;
		private Canvas mBitmapCanvas = null;
	    
	    public PaintBoard(Context context, AttributeSet attrs) {
	        super(context, attrs);
	       
	    }
	    public void init(int width,int height,int bg_color,int fg_color){
	        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
	        mBitmapCanvas = new Canvas(mBitmap);
	        mBitmapCanvas.drawColor(bg_color);//Color.GRAY
	        mPaint = new Paint();
	        mPaint.setColor(fg_color);//Color.RED
	        mPaint.setStrokeWidth(2);
	    }
	        private float startX;
	        private float startY ;
	        @Override
	        public boolean onTouchEvent(MotionEvent event) {

	            switch (event.getAction()) {
	                case MotionEvent.ACTION_DOWN:
	                    startX = event.getX();
	                    startY = event.getY();
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    float stopX = event.getX();
	                    float stopY = event.getY();
	                    Log.w(TAG,"onTouchEvent-ACTION_MOVE\nstartX is "+startX+
	                    " startY is "+startY+" stopX is "+stopX+ " stopY is "+stopY);
	                    mBitmapCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
	                    startX = event.getX();
	                    startY = event.getY();
	                    invalidate();//call onDraw()
	                    break;
	            }
	            return true;
	        }
	        @Override
	        protected void onDraw(Canvas canvas) {
	            if(mBitmap != null) {
	                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
	            }
	        }
	        public void saveBitmap(OutputStream stream) {
	            if (mBitmap != null) {
	                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	            }
	        }
	        public void setForgColor(int c){
	        	mPaint.setColor(c);//Color.RED
	        }
	        public void setBgColor(int c){
	        	mBitmapCanvas.drawColor(c);;//Color.RED
	        }	  
	        public void Clear(int c){
	        	mBitmapCanvas.drawColor(c);//Color.RED
	        	this.invalidate();	        	
	        }
	        public void setStrokeWidth(int width){
	        	mPaint.setStrokeWidth(width);
	        }
	        public void setEraser(int color,int width){
	        	mPaint.setColor(color);//Color.RED
	        	mPaint.setStrokeWidth(width);
	        }
	        public void test(Activity t){	        	
	        	Toast.makeText(t,mBitmap.getWidth()+","+ mBitmap.getHeight(), Toast.LENGTH_SHORT).show();
	        }
	        
	        

	private static int readPictureDegree(String path) {

       
		       int degree  = 0;  
		       try {  
		               ExifInterface exifInterface = new ExifInterface(path);  
		               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
		               switch (orientation) {  
		               case ExifInterface.ORIENTATION_ROTATE_90:  
		                       degree = 90;  
		                       break;  
		               case ExifInterface.ORIENTATION_ROTATE_180:  
		                       degree = 180;  
		                       break;  
		               case ExifInterface.ORIENTATION_ROTATE_270:  
		                       degree = 270;  
		                       break;  
		               }  
		       } catch (IOException e) {  
		               e.printStackTrace();  
		       }  
		       return degree;  
		   } 

	 //int degree1 = readPictureDegree(filePath);
    //Bitmap bm = rotateBitmap(bm,degree1) ;
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){
			if(bitmap == null)
				return null ;
			
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			// Setting post rotate to 90
			Matrix mtx = new Matrix();
			mtx.postRotate(rotate);
			return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		}
	
	public static Bitmap getSmallBitmap(String filePath) {
    	
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if(bm == null){
			return  null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm,degree) ;
		ByteArrayOutputStream baos = null ;
		try{
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
			
		}finally{
			try {
				if(baos != null)
					baos.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm ;

		
	}
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}
	}