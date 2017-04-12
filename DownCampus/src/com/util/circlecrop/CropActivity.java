package com.util.circlecrop;

import java.io.ByteArrayOutputStream;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.mobilecampus.R;
import com.logan.actme.EditDataActivity;
import com.util.TitleBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

@ContentView(R.layout.me_editdata_crop)
public class CropActivity extends Activity {
	@ViewInject(R.id.title_bar)
	private TitleBar titlebar;
	private ImageView mCollectView;
	
	@ViewInject(R.id.id_clipImageLayout)
	private ClipImageLayout mClipImageLayout;
	
	private Intent mIntent;
	private Bundle bundle;
	private String str_uri;
	ClipImageLayout cir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		x.view().inject(this);
		bundle=getIntent().getExtras();
		str_uri=bundle.getString("uri");
		translateNews();
		
		titlebar.setTitle("裁剪");
		titlebar.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mCollectView = (ImageView) titlebar.addAction(new TitleBar.ImageAction(R.mipmap.nav_btn_add) {
            @Override
            public void performAction(View view) {
            	/*mIntent = new Intent();
    			mIntent.putExtra("cam", 4);
    			mIntent.setClass(EditDataActivity.this, MainActivity.class);
    			startActivity(mIntent);
    			finish();*/
    			
    			Bitmap bitmap = mClipImageLayout.clip();
    			ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    			byte[] datas = baos.toByteArray();    			
    			mIntent = new Intent(CropActivity.this, EditDataActivity.class);
    			mIntent.putExtra("bitmap", datas);
    			startActivity(mIntent);
            }
        });
		
	}

	private void translateNews() {
		//cir.mZoomImageView.setImageDrawable();
		Bitmap bm=getBitmapFromUri(Uri.parse(str_uri));
		cir.mZoomImageView.setImageBitmap(bm);
	}
	
	private Bitmap getBitmapFromUri(Uri uri)
	 {
	  try
	  {
	   // 读取uri所在的图片
	   Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
	   return bitmap;
	  }
	  catch (Exception e)
	  {
	   Log.e("[Android]", e.getMessage());
	   Log.e("[Android]", "目录为：" + uri);
	   e.printStackTrace();
	   return null;
	  }
	 }
}
