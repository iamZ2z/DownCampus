package com.util.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.mobilecampus.R;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.logan.actme.EditDataActivity;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Z2z on 2017/3/19.
 */

@ContentView(R.layout.activity_crop)
public class CropViewActivity extends Activity {
    private CropImageView mImageView;
    //private ExecutorService mExecutor;
    private Uri sourceUri;

    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private ImageView mCollectView;

    private Intent mIntent;

    EditDataActivity mEditDataActivity;

    public static Intent createIntent(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, CropViewActivity.class);
        intent.setData(uri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        sourceUri=Uri.parse(getIntent().getStringExtra("uri"));
        titlebar.setTitle("编辑资料");
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
                startActivity(mIntent);
                finish();*/
                mImageView.startCrop(sourceUri, new CropCallback() {
                    @Override
                    public void onSuccess(Bitmap cropped) {

                    }

                    @Override
                    public void onError() {

                    }
                }, new SaveCallback() {
                    @Override
                    public void onSuccess(Uri outputUri) {
                        mEditDataActivity.startResultActivity(sourceUri);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });

        mImageView = (CropImageView) findViewById(R.id.result_image);
        mImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        mImageView.startLoad(
                sourceUri,
                new LoadCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }
}