package com.pjpz.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pjpz.R;
import com.pjpz.adapter.CommentAdapter;
import com.pjpz.model.Comment;
import com.pjpz.utils.ToastUtils;

public class CommentFragment extends Fragment {
	private ListView commentList;
	private EditText etxContent;
	private Button btnComment;
	private List<Comment> comments;
	private CommentAdapter commentAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_comment, container, false);
		commentList = (ListView) view.findViewById(R.id.commentList);
		getComments();
		commentAdapter = new CommentAdapter(getActivity(), comments);
		commentList.setAdapter(commentAdapter);
		etxContent = (EditText) view.findViewById(R.id.etx_content);
		btnComment = (Button) view.findViewById(R.id.btn_comment);
		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = etxContent.getText().toString();
				if (content == null || "".equals(content)) {
					ToastUtils.showShort("请输入内容");
					return;
				}
				Comment comment = new Comment();
				comment.setUserName("it's me");
				comment.setContent(etxContent.getText().toString());
				comments.add(0, comment);
				etxContent.setText("");
				commentAdapter.notifyDataSetChanged();
				commentList.setSelection(0);
			}

		});
		return view;
	}

	private void getComments() {
		comments = new ArrayList<Comment>();
		Comment comment;
		for (int i = 0; i < 6; i++) {
			comment = new Comment();
			comment.setUserName("用户00" + (i + 1));
			comment.setContent("很有感触");
			if (i % 2 == 0) {
				comment.setReply("有品位");
			}
			comments.add(comment);
		}

	}
}
