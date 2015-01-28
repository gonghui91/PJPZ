package com.pjpz.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pjpz.R;

public class CustomProgressDialog extends Dialog {
	public Context context;// 上下文


	public CustomProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		this.context = context;
		this.setCancelable(false);
		View view = LayoutInflater.from(context).inflate(R.layout.load, null); // 加载自己定义的布局
		ImageView img_loading = (ImageView) view.findViewById(R.id.img_load);
		RelativeLayout img_close = (RelativeLayout) view
				.findViewById(R.id.img_cancel);
		RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils
				.loadAnimation(context, R.anim.refresh); // 加载XML文件中定义的动画
		img_loading.setAnimation(rotateAnimation);// 开始动画
		setContentView(view);// 为Dialoge设置自己定义的布局
		// 为close的那个文件添加事件
		img_close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
