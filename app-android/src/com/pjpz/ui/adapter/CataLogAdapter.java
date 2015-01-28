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
		return catalogs.get(position).articleId;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
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
			holder.num = (TextView) convertView
					.findViewById(R.id.tv_catalogNum);
			holder.name = (TextView) convertView
					.findViewById(R.id.tv_articleName);
			convertView.setTag(holder);
		} else {
			holder = (CatalogViewHolder) convertView.getTag();
		}
		if (catalog.articlePage != null && catalog.articlePage != ""
				&& category.ordinal() == 1) {
			holder.num.setText(catalog.articlePage);
		} else {
			holder.num.setText("" + (position + 1));
		}
		// holder.num.setText(catalog.articleId);
		holder.name.setText(catalog.articleName);
		// holder.name.setText("");
		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("catalog", new Gson().toJson(catalogs));
		// bundle.putString("category", category.name());
		// bundle.putString("articleName", catalog.articleName);
		// bundle.putInt("position", position);
		// switch (category.ordinal()) {
		// case Constants.Text:
		// IntentUtils.startIntent(context, ArticleActivity.class,
		// bundle);
		// break;
		// case Constants.Image:
		// IntentUtils.startIntent(context, ImgArticleActivity.class,
		// bundle);
		// break;
		// }
		// }
		// });
		return convertView;
	}

	class CatalogViewHolder {
		TextView num;
		TextView name;
	}
}
