package com.pjpz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjpz.App;
import com.pjpz.R;
import com.pjpz.data.Constant;
import com.pjpz.ui.ArticleActivity;
import com.pjpz.ui.ImgArticleActivity;

public class CataLogAdapter extends BaseAdapter {
	private Context context;

	public CataLogAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 12;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CatalogViewHolder holder = null;
		if (convertView == null) {
			holder = new CatalogViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.catalog_item, null);
			holder.num = (TextView) convertView
					.findViewById(R.id.tv_catalogNum);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_articleName);
			convertView.setTag(holder);
		} else {
			holder = (CatalogViewHolder) convertView.getTag();
		}
		holder.num.setText("" + (position + 1));
		// holder.name.setText("");
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				switch (App.STATUS) {
				case Constant.Text:
					intent.setClass(context, ArticleActivity.class);
					break;
				case Constant.Image:
					intent.setClass(context, ImgArticleActivity.class);
					break;
				}
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class CatalogViewHolder {
		TextView num;
		TextView name;
	}
}
