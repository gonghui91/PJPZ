package com.pjpz.ui.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.pjpz.R;
import com.pjpz.dao.DownloadsDataHelper;
import com.pjpz.data.Constants;
import com.pjpz.model.DownLoad;
import com.pjpz.ui.DownLoadImageActivity;
import com.pjpz.ui.DownLoadTextActivity;

public class DownLoadAdapter extends BaseAdapter {
	private Context context;
	private List<DownLoad> downLoads;
	private LayoutInflater mInflater;
	private DownloadsDataHelper mDataHelper;

	public DownLoadAdapter(Context context, List<DownLoad> downLoads,
			DownloadsDataHelper mDataHelper) {
		this.context = context;
		this.downLoads = downLoads;
		this.mDataHelper = mDataHelper;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return downLoads.size();
	}

	@Override
	public Object getItem(int position) {
		return downLoads.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DownloadViewHolder viewHolder = null;
		final DownLoad downLoad = downLoads.get(position);
		if (convertView == null) {
			viewHolder = new DownloadViewHolder();
			convertView = mInflater.inflate(R.layout.download_item, null);
			viewHolder.num = (TextView) convertView.findViewById(R.id.tv_num);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_articleName);
			viewHolder.category = (ImageView) convertView
					.findViewById(R.id.tv_category);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (DownloadViewHolder) convertView.getTag();
		}
		viewHolder.num.setText("" + (position + 1));
		viewHolder.name.setText(downLoad.articleName);
		if (downLoad.category.equals(Constants.IMAGE))
			viewHolder.category.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.icon_img));
		else
			viewHolder.category.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.icon_txt));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (downLoad.category.equals(Constants.TEXT)) {
					Intent intent = new Intent(context,
							DownLoadTextActivity.class);
					intent.putExtra("downLoad", downLoad);
					context.startActivity(intent);
				} else if (downLoad.category.equals(Constants.IMAGE)) {
					Intent intent = new Intent(context,
							DownLoadImageActivity.class);
					intent.putExtra("downLoad", downLoad);
					context.startActivity(intent);
				}
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
						.setTitleText("是否删除？")
						.setCancelText("取消")
						.setConfirmText("删除")
						.showCancelButton(true)
						.setCancelClickListener(
								new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										sDialog.cancel();
									}
								})
						.setConfirmClickListener(new OnSweetClickListener() {
							@Override
							public void onClick(
									SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.cancel();
								if (mDataHelper.delete(downLoad) > 0) {
									downLoads.remove(downLoad);
									if (downLoad.category
											.equals(Constants.IMAGE)) {
										File file = new File(
												Constants.DEFAULT_IMAGE_URL
														+ downLoad.id);
										if (file.exists()) {
											file.delete();
										}
									}
									notifyDataSetChanged();
								}
							}
						}).show();
				return true;
			}
		});
		return convertView;
	}

	class DownloadViewHolder {
		TextView num;
		TextView name;
		ImageView category;
	}
}
