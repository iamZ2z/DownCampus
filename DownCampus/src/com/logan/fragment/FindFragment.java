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

import com.example.mobilecampus.R;
import com.logan.actdynamic.CampusActActivity;
import com.logan.actdynamic.CampusNewsActivity;
import com.logan.actdynamic.ClassDynamicActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_tab3)
public class FindFragment extends Fragment implements OnClickListener {
    @ViewInject(R.id.text_campusnews)
    private TextView text_campusnews;
    @ViewInject(R.id.text_campusact)
    private TextView text_campusact;

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
        /*View rootView = inflater.inflate(R.adapter_photo_item.fragment_tab3, container,
                false);
		text_campusnews = (TextView) rootView
				.findViewById(R.id.text_campusnews);
		text_campusact = (TextView) rootView.findViewById(R.id.text_campusact);
		text_classact = (TextView) rootView.findViewById(R.id.text_classact);*/
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
            case R.id.text_campusnews:
                mIntent = new Intent(getActivity(), CampusNewsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.text_campusact:
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
