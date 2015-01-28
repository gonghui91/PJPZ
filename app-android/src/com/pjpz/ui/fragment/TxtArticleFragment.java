package com.pjpz.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.pjpz.R;
import com.pjpz.data.Constants;
import com.pjpz.data.GsonRequest;
import com.pjpz.model.Category;
import com.pjpz.model.OptRequestData;
import com.pjpz.model.TextArticle;
import com.pjpz.model.TextArticle.TextItem;
import com.pjpz.utils.OnSelectListener;
import com.pjpz.view.CustomProgressDialog;
import com.pjpz.widget.SharePopupWindow;

public class TxtArticleFragment extends BaseFragment {
	private Button btn_pre, btn_next;
	private ImageView btn_share, btn_comment, btn_collect, btn_praise;
	private Context context;
	private View view;
	private TextView tv_title, tv_time, tv_author, tv_content;
	private OnSelectListener onSelectListener;
	private String articleId;
	private Category category;
	private CustomProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		view = inflater.inflate(R.layout.layout_txtarticle, container, false);
		// btn_pre = (Button) view.findViewById(R.id.btn_pre);
		// btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_praise = (ImageView) view.findViewById(R.id.btn_praise);
		btn_share = (ImageView) view.findViewById(R.id.btn_share);
		btn_comment = (ImageView) view.findViewById(R.id.btn_comment);
		btn_collect = (ImageView) view.findViewById(R.id.btn_collect);
		tv_title = (TextView) view.findViewById(R.id.tv_titile);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_author = (TextView) view.findViewById(R.id.tv_author);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		// btn_pre.setOnClickListener(clickListener);
		// btn_next.setOnClickListener(clickListener);
		btn_praise.setOnClickListener(clickListener);
		btn_share.setOnClickListener(clickListener);
		btn_comment.setOnClickListener(clickListener);
		btn_collect.setOnClickListener(clickListener);
		Bundle bundle = getArguments();
		articleId = bundle.getString("articleId");
		category = Category.valueOf(bundle.getString("category"));
		progressDialog = new CustomProgressDialog(context);
		loadData();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			onSelectListener = (OnSelectListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_praise:
				opt("praise");
				break;
			// case R.id.btn_pre:
			// break;
			// case R.id.btn_next:
			// break;
			case R.id.btn_share:
				opt("share");
				onSelectListener.onShare(true);
				SharePopupWindow sharePopup = new SharePopupWindow(context,
						clickListener);
				sharePopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				sharePopup.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						onSelectListener.onShare(false);
					}
				});
				break;
			case R.id.btn_comment:
				onSelectListener.onComment();
				break;
			case R.id.btn_collect:
				opt("collect");
				break;
			case R.id.iv_share_wechat:
				
				break;
			case R.id.iv_share_wechatm:
				
				break;
			}
		}

	};

	private void opt(String opt) {

		OptRequestData requestData = new OptRequestData();
		requestData.commandName = "articleopt";
		requestData.commandType = opt;
		requestData.commandDevice = Constants.ANDROID;
		OptRequestData.RequestItem requestItem = new OptRequestData.RequestItem();
		requestItem.articleId = articleId;
		ArrayList<OptRequestData.RequestItem> requestItems = new ArrayList<OptRequestData.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<OptRequestData> gsonRequest = new GsonRequest<OptRequestData>(
				requestData.toJson(), OptRequestData.class,
				new Response.Listener<OptRequestData>() {
					@Override
					public void onResponse(OptRequestData arg0) {

					}
				}, errorListener());
		executeRequest(gsonRequest);

	}

	

	private void loadData() {
		TextArticle.TextArticleRequestData requestData = new TextArticle.TextArticleRequestData();
		requestData.commandName = "article";
		requestData.commandType = category.name();
		requestData.commandDevice = Constants.ANDROID;
		TextArticle.RequestItem requestItem = new TextArticle.RequestItem();
		requestItem.articleId = articleId;
		ArrayList<TextArticle.RequestItem> requestItems = new ArrayList<TextArticle.RequestItem>();
		requestItems.add(requestItem);
		requestData.commandParam = requestItems;
		GsonRequest<TextArticle.TextArticleRequestData> gsonRequest = new GsonRequest<TextArticle.TextArticleRequestData>(
				requestData.toJson(), TextArticle.TextArticleRequestData.class,
				responseListener(), errorListener());
		progressDialog.show();
		executeRequest(gsonRequest);
	}

	private Response.Listener<TextArticle.TextArticleRequestData> responseListener() {
		return new Response.Listener<TextArticle.TextArticleRequestData>() {
			@Override
			public void onResponse(
					final TextArticle.TextArticleRequestData response) {
				progressDialog.dismiss();
				TextArticle articles = response.commandData;
				tv_title.setText(articles.articleName);
				tv_author.setText("");
				tv_time.setText("");
				List<TextItem> items = articles.body;
				if (items!=null&&items.size()>0) {
					TextItem textItem = items.get(0);
					tv_content.setText(textItem.text);
				}
				
			}
		};
	}
}
