package com.pjpz.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pjpz.R;
import com.pjpz.model.Comment;

public class CommentAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> comments;

	public CommentAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentViewHolder holder = null;
		final Comment comment = comments.get(position);
		if (convertView == null) {
			holder = new CommentViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_item, null);
			holder.replyer = (TextView) convertView
					.findViewById(R.id.tv_replyer);
			holder.content = (TextView) convertView
					.findViewById(R.id.tv_replyers);
			holder.replyer2 = (TextView) convertView
					.findViewById(R.id.tv_replyer2);
			holder.content2 = (TextView) convertView
					.findViewById(R.id.tv_admins);
			holder.admin = (LinearLayout) convertView
					.findViewById(R.id.layout_admin);
			convertView.setTag(holder);
		} else {
			holder = (CommentViewHolder) convertView.getTag();
		}
//		holder.replyer.setText(comment.userName + ":");
		holder.replyer.setText("游客:");
		holder.content.setText(comment.content);
		String reply = comment.reply;
		if (reply == null || "".equals(reply)) {
			holder.admin.setVisibility(View.GONE);
		} else {
//			holder.replyer2.setText(comment.userName + ":");
			holder.replyer2.setText("游客:");
			holder.content2.setText(reply);
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	class CommentViewHolder {
		LinearLayout admin;
		TextView replyer;
		TextView content;
		TextView replyer2;
		TextView content2;
	}
}
