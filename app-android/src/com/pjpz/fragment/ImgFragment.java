package com.pjpz.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pjpz.R;
import com.pjpz.adapter.ImgListAdapter;
import com.pjpz.model.Periodical;

public class ImgFragment extends Fragment {
	private View imgLayout;
	private ListView listView;
	private List<Periodical> periodicals;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imgLayout = inflater.inflate(R.layout.layout_showimg, container, false);
		listView = (ListView) imgLayout.findViewById(R.id.imgList);
		getPeridicals();
		listView.setAdapter(new ImgListAdapter(getActivity(), periodicals));
		return imgLayout;
	}

	private void getPeridicals() {
		periodicals = new ArrayList<Periodical>();
		Periodical periodical = null;
		for (int i = 0; i < 5; i++) {
			periodical = new Periodical();
			periodical.setName("2012 壬辰年 秋季版 图片版");
			periodical.setSummary("朱清时：中国科学院士、南方科技大学校长");
			periodical.setSyncTime("更新日期 ： 2014-12-10");
			periodicals.add(periodical);
		}
	}
}
