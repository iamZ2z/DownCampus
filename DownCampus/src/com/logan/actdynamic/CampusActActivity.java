package com.logan.actdynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.CampusActAdapter;
import com.logan.bean.CampusActBean;
import com.logan.net.InterfaceTest;
import com.util.title.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.find_campusact)
public class CampusActActivity extends Activity implements OnItemClickListener {
    private Intent mIntent;
    // 列表
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    private InterfaceTest interfaceTest = new InterfaceTest();
    private List<? extends Map<String, ?>> data;
    @ViewInject(R.id.swiperefresh)
    private SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);
        initView();

        mListView.setOnItemClickListener(this);

        urlcampusact();
        swipe();
    }

    private void initView() {
        titlebar.setTitle("校园活动");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIntent = new Intent(this, CampusActDetailActivity.class);
        mIntent.putExtra("campusact", mHashmap.get(position));
        startActivity(mIntent);
    }

    private void urlcampusact() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0)
                .show();
        String url = interfaceTest.getServerurl() + interfaceTest.getCampusactivityquery();
        String token = interfaceTest.getToken();
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("urlCampusAct的result", "请求数据:" + str);
                        final CampusActBean accountListBean = new Gson().fromJson(str,
                                CampusActBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getData2(accountListBean);
                                CampusActAdapter campusActAdapter = new CampusActAdapter
                                        (CampusActActivity.this, mHashmap);

                                mListView.setAdapter(campusActAdapter);
                                dialog.dismiss();
                                campusActAdapter.notifyDataSetChanged();
                                swiperefresh.setRefreshing(false);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void getData2(CampusActBean bean) {
                mHashmap = new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j++) {
                    mMap = new HashMap<>();
                    mMap.put("img", interfaceTest.getImgurl() + bean.getData().get(j).getImage());
                    mMap.put("title", bean.getData().get(j).getName());
                    mMap.put("type", "活动类型：学校运动场");
                    mMap.put("place", bean.getData().get(j).getAddress());
                    mMap.put("data", bean.getData().get(j).getCreateTime());
                    mMap.put("description", bean.getData().get(j).getDescription());
                    mMap.put("remark", bean.getData().get(j).getRemark());
                    mHashmap.add(mMap);
                }
            }
        }).start();
    }

    private void swipe() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                urlcampusact();
            }
        });
    }
}
