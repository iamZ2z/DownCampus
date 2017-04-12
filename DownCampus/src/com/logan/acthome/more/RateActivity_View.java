package com.logan.acthome.more;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.mobilecampus.R;

public class RateActivity_View extends LinearLayout {
	RadioGroup mRg;
	private Context context;
	private OnGroupBtnClickListener listener;
	
	private String groupBtnName="5分#8分#9分#10分#15分";

	public RateActivity_View(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
		TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.GroupButtonView);
		ta.recycle();
		initContentView();
		initView();
		initData();
	}

	private void initContentView() {
		View.inflate(context, R.layout.view_group, this);
		mRg=(RadioGroup)findViewById(R.id.rg_item_group_btn);
	}

	private void initView() {
		String[] btnNameArr=groupBtnName.split("#");
		if(btnNameArr.length<2)
			throw new RuntimeException("格式不正确");
		
		for(int i=0;i<btnNameArr.length;i++){
		RadioButton rb=(RadioButton)View.inflate(context, R.layout.view_group_button, null);
		rb.setBackgroundResource(R.drawable.bluebutton);
		rb.setId(i);
		rb.setTag(btnNameArr[i]);
		rb.setText(btnNameArr[i]);
		mRg.addView(rb);
		}
	}

	private void initData() {
		mRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb=(RadioButton)findViewById(checkedId);
				if(rb.isChecked()&&listener!=null)
					listener.groupBtnClick(rb.getTag().toString());
			}
		});
	}

	public RateActivity_View(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,0);
	}

	public RateActivity_View(Context context) {
		//super(context);
		this(context,null);
	}

	
	public interface OnGroupBtnClickListener{
		public void groupBtnClick(String code);
	}
	
	public void setOnGroupBtnClickListener(OnGroupBtnClickListener listener){
		this.listener=listener;
	}
}
