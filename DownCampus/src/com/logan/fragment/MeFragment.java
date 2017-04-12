package com.logan.fragment;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.actme.AddressActivity;
import com.logan.actme.ChangePassActivity;
import com.logan.actme.DynamicActivity;
import com.logan.actme.EditDataActivity;
import com.logan.actme.OptionActivity;

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
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		btn_edit.setOnClickListener(this);
		tv_changepass.setOnClickListener(this);
		text_dynamic.setOnClickListener(this);
		text_options.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit:
			mIntent=new Intent(getActivity(),EditDataActivity.class);
			startActivity(mIntent);
			break;
		case R.id.tv_changepass:
			mIntent=new Intent(getActivity(),ChangePassActivity.class);
			startActivity(mIntent);
			break;
		case R.id.text_dynamic:
			mIntent=new Intent(getActivity(),DynamicActivity.class);
			startActivity(mIntent);
			break;
		case R.id.text_options:
			mIntent=new Intent(getActivity(),OptionActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}
}
