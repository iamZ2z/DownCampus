package com.logan.actdynamic;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

import java.util.HashMap;

@ContentView(R.layout.find_campusactdetail2)
public class CampusActDetailActivity extends AppCompatActivity implements OnClickListener {
    private Intent mIntent;

    @ViewInject(R.id.img)
    private ImageView img;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.type)
    private TextView type;
    @ViewInject(R.id.place)
    private TextView place;
    @ViewInject(R.id.data)
    private TextView data;
    @ViewInject(R.id.introduce)
    private TextView introduce;
    @ViewInject(R.id.remark)
    private TextView remark;
    @ViewInject(R.id.appendix)
    private TextView appendix;
    @ViewInject(R.id.download)
    private Button download;
    @ViewInject(R.id.sign)
    private Button sign;

    @ViewInject(R.id.toolbar)
    private android.support.v7.widget.Toolbar titlebar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();

        HashMap<String, Object> mHashMap = (HashMap<String, Object>) getIntent()
                .getSerializableExtra("campusact");
        //img.setImageBitmap((Bitmap) mHashMap.get("img"));
        img.setBackgroundResource(R.drawable.upload);
        title.setText(mHashMap.get("title").toString());
        type.setText(mHashMap.get("type").toString());
        place.setText(mHashMap.get("place").toString());
        data.setText(mHashMap.get("data").toString());
        introduce.setText(mHashMap.get("description").toString());
        remark.setText(mHashMap.get("remark").toString());
        appendix.setText("活动章程附件");

        download.setOnClickListener(this);
        sign.setOnClickListener(this);
    }

    private void initView() {
        /*titlebar.setTitle("");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        setSupportActionBar(titlebar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titlebar.setNavigationIcon(R.mipmap.nav_btn_back);
        //设置监听.必须在setSupportActionBar()之后调用
        titlebar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:

                break;

            case R.id.sign:
                //mIntent = new Intent(this,EnrollActivity.class);
                //startActivity(mIntent);
                finish();
                break;
            default:
                break;
        }
    }
}
