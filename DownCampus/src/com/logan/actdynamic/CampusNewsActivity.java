package com.logan.actdynamic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.adapter.CampusNewsAdapter;
import com.logan.bean.CampusNewsBean;
import com.logan.constant.InterfaceTest;
import com.util.title.TitleBar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.find_campusnews)
public class CampusNewsActivity extends Activity {
    // 列表
    @ViewInject(R.id.list)
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private List<HashMap<String, Object>> mHashmap;
    private HashMap<String, Object> mMap;
    @ViewInject(R.id.title_bar)
    private TitleBar titlebar;
    //@ViewInject(R.id.refresh)
    //private SwipeRefreshLayout refresh;

    private List<? extends Map<String, ?>> data;
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
        InterfaceTest interfaceTest = new InterfaceTest();
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
                                campusNewsAdapter=new CampusNewsAdapter(CampusNewsActivity.this,campuslist);

                                data = getData2(bean);
                                mAdapter = new SimpleAdapter(CampusNewsActivity.this, data,
                                        R.layout.item_campusnews_all, new String[]{"img",
                                        "title", "type", "data", "img2", "title2", "type2",
                                        "data2", "img3", "title3", "type3", "data3", "img4",
                                        "title4", "type4", "data4", "img5", "title5", "type5",
                                        "data5",}, new int[]{R.id.img, R.id.title, R.id.type, R
                                        .id.data, R.id.img2, R.id.title2, R.id.type2, R.id.data2,
                                        R.id.img3, R.id.title3, R.id.type3, R.id.data3, R.id
                                        .img4, R.id.title4, R.id.type4, R.id.data4, R.id.img5, R
                                        .id.title5, R.id.type5, R.id.data5,
                                });
                                mListView.setAdapter(campusNewsAdapter);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void campusnewsadapter(CampusNewsBean bean) {
                campuslist= new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j++) {
                    campusmap= new HashMap<>();
                    campusmap.put("img", bean.getData().get(j).getImage());
                    campusmap.put("title", bean.getData().get(j).getTitle());
                    campusmap.put("place", bean.getData().get(j).getSummary());
                    campusmap.put("date", bean.getData().get(j).getUpdateTime());
                    campusmap.put("clickCount", bean.getData().get(j).getClickCount());
                    campusmap.put("showPage", bean.getData().get(j).getShowPage());
                    campuslist.add(campusmap);
                }
            }

            private List<? extends Map<String, ?>> getData2(CampusNewsBean bean) {
                mHashmap= new ArrayList<>();
                for (int j = 0; j < bean.getData().size(); j += 5) {
                    addContent(R.drawable.upload, bean.getData().get(j).getTitle(), "点击次数：" +
                                    bean.getData().get(j).getClickCount(), bean.getData().get(j)
                                    .getUpdateTime(),
                            R.drawable.upload, bean.getData().get(j + 1).getTitle(), "点击次数：" +
                                    bean.getData().get(j + 1).getClickCount(), bean.getData().get
                                    (j + 1).getUpdateTime(),
                            R.drawable.upload, bean.getData().get(j + 2).getTitle(), "点击次数：" +
                                    bean.getData().get(j + 2).getClickCount(), bean.getData().get
                                    (j + 2).getUpdateTime(),
                            R.drawable.upload, bean.getData().get(j + 3).getTitle(), "点击次数：" +
                                    bean.getData().get(j + 3).getClickCount(), bean.getData().get
                                    (j + 3).getUpdateTime(),
                            R.drawable.upload, bean.getData().get(j + 4).getTitle(), "点击次数：" +
                                    bean.getData().get(j + 4).getClickCount(), bean.getData().get
                                    (j + 4).getUpdateTime());
                }
                return mHashmap;
            }
            private void addContent(int upload, String string, String string2, String string3, int
                    upload2, String string4, String string5, String string6, int upload3, String string7,
                                    String string8, String string9, int upload4, String string10, String
                                            string11, String string12, int upload5, String string13,
                                    String string14, String string15) {
                mMap = new HashMap<>();
                mMap.put("img", upload);
                mMap.put("title", string);
                mMap.put("type", string2);
                mMap.put("data", string3);

                mMap.put("img2", upload2);
                mMap.put("title2", string4);
                mMap.put("type2", string5);
                mMap.put("data2", string6);

                mMap.put("img3", upload3);
                mMap.put("title3", string7);
                mMap.put("type3", string8);
                mMap.put("data3", string9);

                mMap.put("img4", upload4);
                mMap.put("title4", string10);
                mMap.put("type4", string11);
                mMap.put("data4", string12);

                mMap.put("img5", upload5);
                mMap.put("title5", string13);
                mMap.put("type5", string14);
                mMap.put("data5", string15);
                mHashmap.add(mMap);
            }
        }).start();
    }
}
