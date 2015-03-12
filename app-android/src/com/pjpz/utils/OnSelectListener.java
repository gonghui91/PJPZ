package com.pjpz.utils;

public interface OnSelectListener {
	public void onShare(boolean isShareOpen);
	public void setTitle(String title);
	public void onComment();
	public void share(int platform,String title,String url);
}
