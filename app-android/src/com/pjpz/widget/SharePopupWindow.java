package com.pjpz.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.pjpz.R;

public class SharePopupWindow extends PopupWindow {
	private View mMenuView;
	private ImageView iv_share_sina, iv_share_tencent, iv_share_wechat,
			iv_share_wechatm;

	public SharePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		mMenuView = LayoutInflater.from(context).inflate(
				R.layout.layout_sharelist, null);
		iv_share_sina = (ImageView) mMenuView.findViewById(R.id.iv_share_sina);
		iv_share_tencent = (ImageView) mMenuView
				.findViewById(R.id.iv_share_tencent);
		iv_share_wechat = (ImageView) mMenuView
				.findViewById(R.id.iv_share_wechat);
		iv_share_wechatm = (ImageView) mMenuView
				.findViewById(R.id.iv_share_wechatm);
		iv_share_sina.setOnClickListener(itemsOnClick);
		iv_share_tencent.setOnClickListener(itemsOnClick);
		iv_share_wechat.setOnClickListener(itemsOnClick);
		iv_share_wechatm.setOnClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.sharestyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
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
