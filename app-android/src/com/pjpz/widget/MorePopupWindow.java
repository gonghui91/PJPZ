package com.pjpz.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pjpz.R;

public class MorePopupWindow extends PopupWindow {
	private View mMenuView;
	private TextView tv_praise, tv_comment, tv_collect, tv_share;

	public MorePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		mMenuView = LayoutInflater.from(context).inflate(R.layout.layout_more,
				null);
		tv_praise = (TextView) mMenuView.findViewById(R.id.tv_praise);
		tv_comment = (TextView) mMenuView.findViewById(R.id.tv_comment);
		tv_collect = (TextView) mMenuView.findViewById(R.id.tv_collect);
		tv_share = (TextView) mMenuView.findViewById(R.id.tv_share);
		tv_praise.setOnClickListener(itemsOnClick);
		tv_comment.setOnClickListener(itemsOnClick);
		tv_collect.setOnClickListener(itemsOnClick);
		tv_share.setOnClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
}
