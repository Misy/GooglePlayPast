package com.wish.googleplay.fragment;

import java.util.List;

import com.wish.googleplay.adapter.DefaultAdapter;
import com.wish.googleplay.domain.CategoryInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.holder.CategoryContentHolder;
import com.wish.googleplay.protocol.CategoryProtocol;
import com.wish.googleplay.tools.UiUtils;
import com.wish.googleplay.view.BaseListView;
import com.wish.googleplay.view.LoadingPage.LoadResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryFragment extends BaseFragment {

	private List<CategoryInfo> datas;

	@Override
	public View createSuccessView() {
		BaseListView listView = new BaseListView(UiUtils.getContext());
		listView.setAdapter(new CategoryAdapter(datas, listView));
		return listView;
	}

	class CategoryAdapter extends DefaultAdapter<CategoryInfo> {

		public CategoryAdapter(List<CategoryInfo> datas, ListView listView) {
			super(datas, listView);
		}

		@Override
		protected BaseHolder<CategoryInfo> getHolder() {
			return new CategoryContentHolder();
		}

		@Override
		protected boolean hasMore() {
			return false;
		}

		@Override
		protected List<CategoryInfo> onload() {
			return null;
		}

	}

	@Override
	protected LoadResult load() {
		CategoryProtocol protocol = new CategoryProtocol();
		datas = protocol.load(0);
		return checkData(datas);
	}
}
