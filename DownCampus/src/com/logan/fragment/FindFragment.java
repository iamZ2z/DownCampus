package com.logan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.example.mobilecampus.R;
import com.logan.actdynamic.CampusActActivity;
import com.logan.actdynamic.CampusNewsActivity;
import com.logan.actdynamic.ClassDynamicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_tab3)
public class FindFragment extends Fragment implements OnClickListener {
    @ViewInject(R.id.supertv)
    private SuperTextView text_campusnews;
    @ViewInject(R.id.supertv2)
    private SuperTextView text_campusact;

    @ViewInject(R.id.text_classact)
    private TextView text_classact;
    private Intent mIntent;

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
        text_campusnews.setOnClickListener(this);
        text_campusact.setOnClickListener(this);
        text_classact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supertv:
                mIntent = new Intent(getActivity(), CampusNewsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.supertv2:
                mIntent = new Intent(getActivity(), CampusActActivity.class);
                startActivity(mIntent);
                break;
            case R.id.text_classact:
                mIntent = new Intent(getActivity(), ClassDynamicActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

}
