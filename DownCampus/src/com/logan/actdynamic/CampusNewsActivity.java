package com.logan.actdynamic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.CampusNewsAdapter;
import com.logan.bean.CampusNewsBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.find_campusnews)
public class CampusNewsActivity extends Activity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.list)
    private ListView mListView;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;
    private CampusNewsAdapter campusNewsAdapter;
    private List<HashMap<String, Object>> campuslist;
    private HashMap<String, Object> campusmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();
        dourl();
        swipe();
    }

    private void initView() {
        titlebar.setTitle("校园新闻");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dourl() {
        final InterfaceTest interfaceTest = new InterfaceTest();
        String url = interfaceTest.getServerurl() + interfaceTest.getCampusnewsquery();
        String token = interfaceTest.getToken();

        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new OkHttpClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlleave的result", "请求数据:" + str);
                        final CampusNewsBean bean = new Gson().fromJson(str,
                                CampusNewsBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                campusnewsadapter(bean);
                                campusNewsAdapter = new CampusNewsAdapter(CampusNewsActivity
                                        .this, campuslist);
                                mListView.setAdapter(campusNewsAdapter);
                                mListView.setOnItemClickListener(CampusNewsActivity.this);

                                campusNewsAdapter.notifyDataSetChanged();
                                swiperefresh.setRefreshing(false);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void campusnewsadapter(CampusNewsBean bean) {
                campuslist = new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j++) {
                    campusmap = new HashMap<>();
                    campusmap.put("img", interfaceTest.getImgurl() + bean.getData().get(j)
                            .getImage());
                    campusmap.put("title", bean.getData().get(j).getTitle());
                    campusmap.put("place", bean.getData().get(j).getSummary());
                    campusmap.put("date", bean.getData().get(j).getUpdateTime());
                    campusmap.put("clickCount", bean.getData().get(j).getClickCount());
                    campusmap.put("showPage", bean.getData().get(j).getShowPage());
                    campuslist.add(campusmap);
                }
            }
        }).start();
    }

    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dourl();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CampusNewsDetailActivity.class);
        String str=campuslist.get(position).get("showPage").toString();
        intent.putExtra("campusnews", str);
        startActivity(intent);
    }

}
