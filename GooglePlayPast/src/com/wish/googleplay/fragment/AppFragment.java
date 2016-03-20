package com.wish.googleplay.fragment;

import java.util.List;

import com.wish.googleplay.adapter.DefaultAdapter;
import com.wish.googleplay.adapter.ListBaseAdapter;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.protocol.AppProtocol;
import com.wish.googleplay.protocol.HomeProtocol;
import com.wish.googleplay.tools.UiUtils;
import com.wish.googleplay.view.BaseListView;
import com.wish.googleplay.view.LoadingPage.LoadResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class AppFragment extends BaseFragment {
	private List<AppInfo> datas;

	@Override
	public View createSuccessView() {
		BaseListView listView = new BaseListView(UiUtils.getContext());
		listView.setAdapter(new ListBaseAdapter(datas, listView) {

			@Override
			protected List<AppInfo> onload() {
				AppProtocol protocol = new AppProtocol();
				List<AppInfo> load = protocol.load(datas.size());
				return load;
			}
		});
		return listView;
	}

	@Override
	protected LoadResult load() {
		AppProtocol protocol = new AppProtocol();
		datas = protocol.load(0);
		return checkData(datas);
	}

}
