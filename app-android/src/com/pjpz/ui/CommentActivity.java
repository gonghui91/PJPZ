package com.pjpz.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Comment;
import com.pjpz.model.Comment.AddCommentRequestData;
import com.pjpz.model.Comment.CommentItem;
import com.pjpz.ui.adapter.CommentAdapter;
import com.pjpz.utils.ToastUtils;
import com.pjpz.view.CustomProgressDialog;

public class CommentActivity extends BaseActivity {
	private ListView commentList;
	private EditText etxContent;
	private Button btnComment;
	private CommentAdapter commentAdapter;
	private CustomProgressDialog progressDialog;
	private String articleId;
	private String category;
	private List<Comment> body = new ArrayList<Comment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_comment);
		initView();
	}

	private void initView() {
		actionBar.setTitle("评论");
		Bundle bundle = getIntent().getExtras();
		articleId = bundle.getString("articleId");
		category = bundle.getString("category");
		commentList = (ListView) findViewById(R.id.commentList);
		progressDialog = new CustomProgressDialog(this);
		etxContent = (EditText) findViewById(R.id.etx_content);
		btnComment = (Button) findViewById(R.id.btn_comment);
		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = etxContent.getText().toString();
				if (content == null || "".equals(content)) {
					ToastUtils.showShort("请输入内容");
					return;
				} else
					addData(etxContent.getText().toString());
			}

		});
		loadData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			CommentActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addData(String content) {
		Comment.AddCommentRequestData requestData = new Comment.AddCommentRequestData();
		requestData.commandName = "comment";
		requestData.commandType = category;
		requestData.commandDevice = Constants.ANDROID;
		Comment.AddItem requestItem = new Comment.AddItem();
		requestItem.articleId = articleId;
		requestItem.content = content;
		requestData.commandParam = requestItem;
		progressDialog.show();
		GsonRequest<Comment.AddCommentRequestData> gsonRequest = new GsonRequest<Comment.AddCommentRequestData>(
				requestData.toJson(), Comment.AddCommentRequestData.class,
				new Response.Listener<Comment.AddCommentRequestData>() {
					@Override
					public void onResponse(AddCommentRequestData response) {
						progressDialog.dismiss();
						if (response.commandStatus.equals(Constants.TRUE)) {
							Comment item = new Comment();
							item.userName = "游客";
							item.content = etxContent.getText().toString();
							body.add(0, item);
							etxContent.setText("");
							commentAdapter.notifyDataSetChanged();
							commentList.setSelection(0);
						}
					}
				}, errorListener());
		executeRequest(gsonRequest);

	}

	private void loadData() {
		Comment.ListCommentRequestData requestData = new Comment.ListCommentRequestData();
		requestData.commandName = "getcomment";
		requestData.commandType = category;
		requestData.commandDevice = Constants.ANDROID;
		Comment.RequestItem requestItem = new Comment.RequestItem();
		requestItem.articleId = articleId;
		requestData.commandParam = requestItem;
		GsonRequest<Comment.ListCommentRequestData> gsonRequest = new GsonRequest<Comment.ListCommentRequestData>(
				requestData.toJson(), Comment.ListCommentRequestData.class,
				responseListener(), errorListener());
		progressDialog.show();
		executeRequest(gsonRequest);
	}

	private Response.Listener<Comment.ListCommentRequestData> responseListener() {
		return new Response.Listener<Comment.ListCommentRequestData>() {
			@Override
			public void onResponse(final Comment.ListCommentRequestData response) {
				progressDialog.cancel();
				if (response.commandStatus.equals("true")) {
					if (response.commandData != null
							&& response.commandData != null) {
						body = response.commandData;
					}
					commentAdapter = new CommentAdapter(CommentActivity.this,
							body);
					commentList.setAdapter(commentAdapter);
				}
			}

		};
	}
}
