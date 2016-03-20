package com.wish.googleplay.adapter;

import java.util.List;

import android.widget.ListView;

import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.holder.ListBaseHolder;

public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> {

	public ListBaseAdapter(List<AppInfo> datas, ListView listView) {
		super(datas, listView);
	}

	@Override
	public BaseHolder<AppInfo> getHolder() {
		return new ListBaseHolder();
	}

	@Override
	protected abstract List<AppInfo> onload();

}
