package com.pjpz.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjpz.R;
import com.pjpz.model.Catalog;
import com.pjpz.model.Category;

public class CataLogAdapter extends BaseAdapter {
	private Context context;
	private List<Catalog> catalogs;
	private Category category;

	public CataLogAdapter(Context context, List<Catalog> catalogs,
			Category category) {
		this.context = context;
		this.catalogs = catalogs;
		this.category = category;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return catalogs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return catalogs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CatalogViewHolder holder = null;
		final Catalog catalog = catalogs.get(position);
		if (convertView == null) {
			holder = new CatalogViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.catalog_item, null);
			holder.first = (TextView) convertView
					.findViewById(R.id.tv_firstname);
			holder.second = (TextView) convertView
					.findViewById(R.id.tv_secondname);
			holder.num = (TextView) convertView
					.findViewById(R.id.tv_catalogNum);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_articleName);
			convertView.setTag(holder);
		} else {
			holder = (CatalogViewHolder) convertView.getTag();
		}
//		System.out.println("catalog.first--"+catalog.first);
		holder.first.setText(catalog.first);
//		if (catalog.first != null) {
			if (catalog.first != null&&(position == 0||!catalog.first.equals(((Catalog) getItem(position - 1)).first))) {
				holder.first.setVisibility(View.VISIBLE);
			}
			else{
				holder.first.setVisibility(View.GONE);
			}
//		}
//		if (position == 0) {
//			holder.first.setVisibility(View.VISIBLE);
//		} else {
//			if (catalog.first != null && !catalog.first.equals(((Catalog) getItem(position - 1)).first)) {
//				holder.first.setVisibility(View.VISIBLE);
//			} else {
//				holder.first.setVisibility(View.GONE);
//			}
//		}
		if (category.ordinal() == 1) {
			holder.second.setText(catalog.second);
			if (catalog.second != null&&(position == 0||!catalog.second.equals(((Catalog) getItem(position - 1)).second))) {
				holder.second.setVisibility(View.VISIBLE);
			}else{
				holder.second.setVisibility(View.GONE);
			}
//			if (position == 0) {
//				holder.second.setVisibility(View.VISIBLE);
//			} else {
//				String presecond = ((Catalog) getItem(position - 1)).second;
//				if (catalog.second != null && !catalog.second.equals(presecond)) {
//					holder.second.setVisibility(View.VISIBLE);
//				} else {
//					holder.second.setVisibility(View.GONE);
//				}
//			}
		}
		if (catalog.articlePage != null && catalog.articlePage != ""
				&& category.ordinal() == 1) {
			holder.num.setText(catalog.articlePage);
		} else {
			holder.num.setText("" + (position + 1));
		}
		holder.name.setText(catalog.articleName);
		return convertView;
	}

	class CatalogViewHolder {
		TextView first;
		TextView second;
		TextView num;
		TextView name;
	}
}
