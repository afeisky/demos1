package com.afei.paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.afei.paint.R;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
/**
 * Created by chaofei on 2018/02/06
 */
public class MainActivity extends Activity implements OnClickListener{
	private String TAG="MainActivity";
	
	private PaintBoard paintBoard;
	private Button btn_save;
	private Button btn_setting;
	private Button btn_clear;
	private Button btn_eraser;
	private ImageButton btn_bg;
	private ImageButton btn_color1;
	private ImageButton btn_color2;
	private ImageButton btn_color3;
	private ImageButton btn_color4;
	private ImageButton btn_color5;
	private ImageButton btn_color6;
	private int paint_color1=Color.WHITE;
	private int paint_color2=Color.BLACK;
	private int paint_color3=Color.RED;
	private int paint_color4=Color.BLUE;
	private int paint_color5=Color.GREEN;
	private int paint_color6=Color.YELLOW;	
	private int bg_color=Color.WHITE;
	private int fg_color=Color.BLACK;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_h);
		Log.w(TAG,"Chaofei.wu QQ650827157 add 2018-02-07: onCreate()");
		paintBoard = (PaintBoard)findViewById(R.id.canvas1);
		btn_color1 = (ImageButton) findViewById(R.id.btn_color1);
		btn_color1.setOnClickListener(this);
		btn_color2 = (ImageButton) findViewById(R.id.btn_color2);
		btn_color2.setOnClickListener(this);
		btn_color3 = (ImageButton) findViewById(R.id.btn_color3);
		btn_color3.setOnClickListener(this);
		btn_color4 = (ImageButton) findViewById(R.id.btn_color4);
		btn_color4.setOnClickListener(this);
		btn_color5 = (ImageButton) findViewById(R.id.btn_color5);
		btn_color5.setOnClickListener(this);
		btn_color6 = (ImageButton) findViewById(R.id.btn_color6);
		btn_color6.setOnClickListener(this);		
		btn_eraser = (Button) findViewById(R.id.btn_eraser);
		btn_eraser.setOnClickListener(this);
		btn_bg = (ImageButton) findViewById(R.id.btn_bg);
		btn_bg.setOnClickListener(this);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(this);

		DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        int widthPixels=displayMetrics.widthPixels;
        paintBoard.init(widthPixels,heightPixels,bg_color,fg_color);
		initColors(Color.WHITE);
		paintBoard.Clear(bg_color);
		paintBoard.setStrokeWidth(2);
		
		Log.w(TAG,"screen:widthPixels="+widthPixels+", heightPixels="+heightPixels+"\n");
		
		//paintBoard.test(this);
	}
	
	 private void initColors(int color){
		 if (color==Color.WHITE){
	 			bg_color=Color.WHITE;
	 			fg_color=Color.BLACK; 
	 			paint_color1=Color.WHITE;
	 			paint_color2=Color.BLACK;
	 			paint_color3=Color.RED;
	 			paint_color4=Color.BLUE;
	 			paint_color5=Color.GREEN;
	 			paint_color6=Color.YELLOW;         			
	         			
 		}else{
			 bg_color=Color.BLACK;
			 fg_color=Color.WHITE; 	
			paint_color1=Color.BLACK;
			paint_color2=Color.WHITE;
			paint_color3=Color.RED;
			paint_color4=Color.BLUE;
			paint_color5=Color.GREEN;
			paint_color6=Color.YELLOW;        			
			
 		}
	 		setBtnColors(1,paint_color1);
	 		setBtnColors(2,paint_color2);
	 		setBtnColors(3,paint_color3);
	 		setBtnColors(4,paint_color4);
	 		setBtnColors(5,paint_color5);
	 		setBtnColors(6,paint_color6);	 
	 		setBtnColors(99,bg_color);	
	 		setPaintColors(1);
	 		setPaintColors(2);
	 		setPaintColors(3);
	 		setPaintColors(4);
	 		setPaintColors(5);
	 		setPaintColors(6);

			paintBoard.setForgColor(fg_color);
			//paintBoard.setBgColor(bg_color);	
		 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private SimpleDateFormat yyyy_mm_dd_hhmmss = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
	 public void OnSaveClicked(View view) {
	        try {
	        	
	            File f = Environment.getExternalStorageDirectory();
	            String filepathname= f.getAbsolutePath() + "/"+dirname;
	            f = new File(filepathname);
	            if (!f.exists()) {
	                f.mkdir();
	            }
	            if (f.exists()) {		            
		            String sdate=yyyy_mm_dd_hhmmss.format(new Date());
		            File file = new File(filepathname,"Paint_"+sdate + ".jpg");
		            OutputStream stream = new FileOutputStream(file);
		            paintBoard.saveBitmap(stream);
		            stream.close();
		            // send broadcast to Media to update data
		            if (Build.VERSION.SDK_INT <=14 ) { 
		            	 Intent intent = new Intent();
				         intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
				         intent.setData(Uri.fromFile(f));//intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
				         sendBroadcast(intent);
		            } else{ //Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4   判断SDK版本是不是4.4或者高于4.4  
		            	String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};  
		                MediaScannerConnection.scanFile(this.getApplicationContext(), paths, null, null);  
		            } 
		           
		            Toast.makeText(this, "Save success\n"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
	            }else{
	            	Toast.makeText(this, "Error: Don't mkdir "+dirname, Toast.LENGTH_SHORT).show();
	            }	            
	        } catch (Exception e) {
	            Toast.makeText(this, "Error: save failed\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
	        }
	 }
	 private String dirname="PaintABC_DangDang";
	 
	 private void testDir(){
		 File f = Environment.getExternalStorageDirectory();  
		 Log.w(TAG,"111");         
         OutputStreamWriter out=null;
         try {
        	File flog = new File(Environment.getExternalStorageDirectory(),"PaintABC_DangDang.txt");
        	if (!flog.exists()) {
        		flog.createNewFile();
        	}
			out = new OutputStreamWriter(new FileOutputStream(flog.getAbsoluteFile()), "utf-8");
	        File[] flist = f.listFiles();
	        Log.w(TAG,"333");
	        if (flist==null ){
	        	Log.w(TAG,"no files in "+f.getAbsolutePath()+"\n");
	        }else{
		        for (int i = 0; i < flist.length; i++) {
		        	 String str=flist[i].getAbsolutePath();
		        	 Log.w(TAG,str+"\n");
		        	 out.write(str+"\n");
		        }
	        }
	        
	        String filepathname= f.getAbsolutePath() + "/"+dirname;
	        f = new File(filepathname);
	         if (!f.exists()) {
		         Toast.makeText(this, filepathname, Toast.LENGTH_SHORT).show();
	             f.mkdir();
	         }
	        flist = f.listFiles();
	        if (flist==null ){
	        	Log.w(TAG,"no files in "+f.getAbsolutePath()+"\n");
	        }else{
		        for (int i = 0; i < flist.length; i++) {
		        	 String str=flist[i].getAbsolutePath();
		        	 Log.w(TAG,str+"\n");
		        	 out.write(str+"\n");
		        }	        
	        }
	        
	        //----
	        out.close();

		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

	
	private void setPaintColors(int index){
		switch (index){
		case 1:
			paintBoard.setForgColor(paint_color1);
			break;
		case 2:
			paintBoard.setForgColor(paint_color2);
			break;
		case 3:
			paintBoard.setForgColor(paint_color3);
			break;
		case 4:
			paintBoard.setForgColor(paint_color4);
			break;
		case 5:
			paintBoard.setForgColor(paint_color5);
			break;
		case 6:
			paintBoard.setForgColor(paint_color6);
			break;			
		}
		paintBoard.setStrokeWidth(2);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {  
        	case R.id.btn_color1:
        		setPaintColors(1);
        		break;
        	case R.id.btn_color2:
        		setPaintColors(2);
        		break;
        	case R.id.btn_color3:
        		setPaintColors(3);
        		break;
        	case R.id.btn_color4:
        		setPaintColors(4);
        		break;
        	case R.id.btn_color5:
        		setPaintColors(5);
        		break;
        	case R.id.btn_color6:
        		setPaintColors(6);
        		break;          		
        	//---
        	case R.id.btn_eraser:
        		paintBoard.setEraser(bg_color,10);
        		break;       
        	case R.id.btn_bg:
        		initColors(bg_color==Color.WHITE?Color.BLACK:Color.WHITE);
        		break;       
        	case R.id.btn_clear:
        		paintBoard.Clear(bg_color);
        		paintBoard.setForgColor(fg_color);
        		paintBoard.setStrokeWidth(2);
        		break;       
        	case R.id.btn_save:
        		OnSaveClicked(v);
        		break;      
        	case R.id.btn_setting:
        		Toast.makeText(this, "email: 650827157@qq.com", Toast.LENGTH_SHORT).show();
        		break;           		
		}
		
	}

	private void setBtnColors(int index,int color){
		switch (index){
		case 1:
			paint_color1=color;
			btn_color1.setImageDrawable(new ColorDrawable(paint_color1));
			break;
		case 2:
			paint_color2=color;
			btn_color2.setImageDrawable(new ColorDrawable(paint_color2));
			break;
		case 3:
			paint_color3=color;
			btn_color3.setImageDrawable(new ColorDrawable(paint_color3));
			break;
		case 4:
			paint_color4=color;
			btn_color4.setImageDrawable(new ColorDrawable(paint_color4));
			break;
		case 5:
			paint_color5=color;
			btn_color5.setImageDrawable(new ColorDrawable(paint_color5));
			Log.w(TAG,"setBtnColors"+index);
			break;
		case 6:
			paint_color6=color;
			btn_color6.setImageDrawable(new ColorDrawable(paint_color6));
			Log.w(TAG,"setBtnColors"+index);
			break;		
		case 99:
			bg_color=color;
			btn_bg.setImageDrawable(new ColorDrawable(bg_color));
			Log.w(TAG,"setBtnColors  btn_bg="+index);
			break;				
		}
		paintBoard.setStrokeWidth(2);
	} 
}
