package com.wish.googleplay.adapter;

import java.util.List;

import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.wish.googleplay.DetailActivity;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.holder.ListBaseHolder;
import com.wish.googleplay.tools.UiUtils;

public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> {

	public ListBaseAdapter(List<AppInfo> datas, ListView listView) {
		super(datas, listView);
	}

	@Override
	public BaseHolder<AppInfo> getHolder() {
		return new ListBaseHolder();
	}

	@Override
	public void onInnerItemClick(int position) {
		super.onInnerItemClick(position);
		Toast.makeText(UiUtils.getContext(), "position:" + position, 0).show();
		// 跳转到DetailActivity
		Intent intent = new Intent(UiUtils.getContext(), DetailActivity.class);
		AppInfo appInfo = datas.get(position);
		intent.putExtra("packageName", appInfo.getPackageName());
		UiUtils.startActivity(intent);
	}

	@Override
	protected abstract List<AppInfo> onload();

}
