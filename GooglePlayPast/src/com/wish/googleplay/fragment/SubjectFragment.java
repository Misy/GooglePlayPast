package com.wish.googleplay.fragment;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.wish.googleplay.R;
import com.wish.googleplay.adapter.DefaultAdapter;
import com.wish.googleplay.domain.SubjectInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.protocol.SubjectProtocol;
import com.wish.googleplay.tools.BitmapHelper;
import com.wish.googleplay.tools.UiUtils;
import com.wish.googleplay.view.BaseListView;
import com.wish.googleplay.view.LoadingPage.LoadResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectFragment extends BaseFragment {

	private List<SubjectInfo> datas;

	@Override
	public View createSuccessView() {
		BaseListView listView = new BaseListView(UiUtils.getContext());
		listView.setAdapter(new SubjectAdapter(datas, listView));
		return listView;
	}

	private class SubjectAdapter extends DefaultAdapter<SubjectInfo> {

		public SubjectAdapter(List<SubjectInfo> datas, ListView listView) {
			super(datas, listView);
		}

		@Override
		public BaseHolder<SubjectInfo> getHolder() {
			return new SubjectHolder();
		}

		@Override
		protected List<SubjectInfo> onload() {
			SubjectProtocol protocol = new SubjectProtocol();
			List<SubjectInfo> load = protocol.load(datas.size());
			datas.addAll(load);
			return load;
		}

	}

	class SubjectHolder extends BaseHolder<SubjectInfo> {
		ImageView item_icon;
		TextView item_txt;

		@Override
		public View initView() {
			View contentView = UiUtils.inflate(R.layout.item_subject);
			this.item_icon = (ImageView) contentView
					.findViewById(R.id.item_icon);
			this.item_txt = (TextView) contentView.findViewById(R.id.item_txt);
			return contentView;
		}

		@Override
		public void refreshView(SubjectInfo data) {
			this.item_txt.setText(data.getDes());
			bitmapUtils.display(this.item_icon, HttpHelper.URL + "image?name="
					+ data.getUrl());
		}

	}

	@Override
	protected LoadResult load() {
		SubjectProtocol protocol = new SubjectProtocol();
		datas = protocol.load(0);
		return checkData(datas);
	}
}
