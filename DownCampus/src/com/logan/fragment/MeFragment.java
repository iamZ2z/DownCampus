package com.logan.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilecampus.R;
import com.google.gson.Gson;
import com.logan.actme.ChangePassActivity;
import com.logan.actme.DynamicActivity;
import com.logan.actme.EditDataActivity;
import com.logan.actme.OptionActivity;
import com.logan.bean.MeFragmentBean;
import com.logan.constant.InterfaceTest;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.fragment_tab4)
public class MeFragment extends Fragment implements OnClickListener {
    @ViewInject(R.id.btn_edit)
    private TextView btn_edit;
    @ViewInject(R.id.tv_changepass)
    private LinearLayout tv_changepass;
    @ViewInject(R.id.text_dynamic)
    private LinearLayout text_dynamic;
    @ViewInject(R.id.text_options)
    private LinearLayout text_options;
    private Intent mIntent;

    @ViewInject(R.id.btn_changerole)
    private Button btn_changerole;
    private InterfaceTest interfaceTest = new InterfaceTest();
    //还没获取接口图片

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_edit.setOnClickListener(this);
        tv_changepass.setOnClickListener(this);
        text_dynamic.setOnClickListener(this);
        text_options.setOnClickListener(this);

        if (interfaceTest.getRole().equals("家长")) urlchange();
    }

    private void urlchange() {
        String url = interfaceTest.getServerurl() + interfaceTest.getParentschild();
        String userid = interfaceTest.getUser_id();
        String token = interfaceTest.getToken();

        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("token", token).add("parentId", userid)
                .build();
        final Request request = new Request.Builder().url(url).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        Log.e("url家长所有孩子的result", "请求数据:" + str);
                        Gson gson = new Gson();
                        final MeFragmentBean accountListBean = gson.fromJson(str, MeFragmentBean
                                .class);
                        if (accountListBean.getData().size() != 0 || accountListBean.getData()
                                .size() != 1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_changerole.setVisibility(View.VISIBLE);
                                    btn_changerole.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            changerole(accountListBean);
                                        }
                                    });
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void changerole(MeFragmentBean accountListBean) {
        final String[] strarr = new String[accountListBean.getData().size()];
        final String[] studentid = new String[accountListBean.getData().size()];
        for (int j = 0; j < accountListBean.getData().size(); j++) {
            strarr[j] = accountListBean.getData().get(j).getFullname();
            studentid[j] = accountListBean.getData().get(j).getStudentId();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(null);
        builder.setSingleChoiceItems(strarr, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "点击了" + strarr[which], Toast.LENGTH_SHORT).show();
                interfaceTest.setStudentId(studentid[which]);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                mIntent = new Intent(getActivity(), EditDataActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_changepass:
                mIntent = new Intent(getActivity(), ChangePassActivity.class);
                startActivity(mIntent);
                break;
            case R.id.text_dynamic:
                mIntent = new Intent(getActivity(), DynamicActivity.class);
                startActivity(mIntent);
                break;
            case R.id.text_options:
                mIntent = new Intent(getActivity(), OptionActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
