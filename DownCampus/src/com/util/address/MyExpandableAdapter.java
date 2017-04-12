package com.util.address;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilecampus.R;
import com.logan.actnews.OtherActivity;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<GroupMemberBean> mGroup;
	private List<List<GroupMemberBean>> mChild;
	private LayoutInflater mInflater;

	public MyExpandableAdapter(Context mContext, List<GroupMemberBean> mGroup,
			List<List<GroupMemberBean>> mChild) {
		this.mContext = mContext;
		this.mGroup = mGroup;
		this.mChild = mChild;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<GroupMemberBean> mGroup,
			List<List<GroupMemberBean>> mChild) {
		this.mGroup = mGroup;
		this.mChild = mChild;
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mChild.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mGroup.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mChild.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupHolderView groupHolderView;
		if (convertView == null) {
			groupHolderView = new GroupHolderView();
			convertView = (View) mInflater.inflate(R.layout.item_group_address,
					null);
			groupHolderView.groupTv = (TextView) convertView
					.findViewById(R.id.exlist_groupname);
			convertView.setTag(groupHolderView);
		} else {
			groupHolderView = (GroupHolderView) convertView.getTag();
		}
		groupHolderView.groupTv.setText(mGroup.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildHolderView childHolderView;
		if (convertView == null) {
			childHolderView = new ChildHolderView();
			convertView = (View) mInflater.inflate(R.layout.item_address, null);
			childHolderView.childIv = (ImageView) convertView
					.findViewById(R.id.exlist_itemimg);
			childHolderView.childTv = (TextView) convertView
					.findViewById(R.id.exlist_itemname);
			childHolderView.childLl = (LinearLayout) convertView
					.findViewById(R.id.exlist_itemlayout);
			convertView.setTag(childHolderView);
		} else {
			childHolderView = (ChildHolderView) convertView.getTag();
		}
		childHolderView.childTv.setText(mChild.get(groupPosition)
				.get(childPosition).getName());
		childHolderView.childLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Toast.makeText(
						mContext,
						"group:"
								+ mGroup.get(groupPosition).getName()
								+ "->child:"
								+ mChild.get(groupPosition).get(childPosition)
										.getName(), Toast.LENGTH_SHORT).show();*/
				Intent mIntent=new Intent(mContext,OtherActivity.class);
				Bundle mBundle=new Bundle();
				mBundle.putString("address_name", mChild.get(groupPosition).get(childPosition).getName());
				mIntent.putExtras(mBundle);
				mContext.startActivity(mIntent);
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class GroupHolderView {
		TextView groupTv;
	}

	class ChildHolderView {
		ImageView childIv;
		TextView childTv;
		LinearLayout childLl;
	}

}
