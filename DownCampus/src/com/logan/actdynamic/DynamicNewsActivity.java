package com.logan.actdynamic;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Z2z on 2017-4-17.
 */

@ContentView(R.layout.find_dynamicnews)
public class DynamicNewsActivity extends Activity {
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.nonews)
    private TextView nonews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.setActionTextColor(Color.WHITE);
        titlebar.addAction(new TitleBar.TextAction("清空") {
            @Override
            public void performAction(View view) {
                for(int i=0;i<mHashmap.size();i++)
                mHashmap.remove(i);
                mAdapter.notifyDataSetChanged();
                nonews.setVisibility(View.VISIBLE);
            }
        });

        initNews();
    }

    private void initNews() {
        mAdapter = new SimpleAdapter(this, getData(), R.layout.item_dynamicnews,
                new String[]{"icon", "name", "reply", "time", "content"},
                new int[]{R.id.icon, R.id.name, R.id.reply, R.id.leavetime,
                        R.id.content});
        mListView.setAdapter(mAdapter);
        //mListView.setOnItemClickListener(this);
    }

    private List<HashMap<String, Object>> getData() {
        mHashmap = new ArrayList<>();
        mMap = new HashMap<>();

        mMap.put("icon", R.drawable.upload);
        mMap.put("name", "希望小学帝第");
        mMap.put("reply", "hahaha");
        mMap.put("time", "2017-04-17");
        mMap.put("content", "网易云很感人");
        mHashmap.add(mMap);
        return mHashmap;
    }

}
