package com.wish.googleplay.fragment;

import java.util.List;

import com.wish.googleplay.adapter.DefaultAdapter;
import com.wish.googleplay.domain.CategoryInfo;
import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.holder.CategoryContentHolder;
import com.wish.googleplay.holder.CategoryTitleHolder;
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

	public static final int ITEM_TITLE = 2;
	private List<CategoryInfo> datas;

	@Override
	public View createSuccessView() {
		BaseListView listView = new BaseListView(UiUtils.getContext());
		listView.setAdapter(new CategoryAdapter(datas, listView));
		return listView;
	}

	class CategoryAdapter extends DefaultAdapter<CategoryInfo> {
		private int position;

		public CategoryAdapter(List<CategoryInfo> datas, ListView listView) {
			super(datas, listView);
		}

		@Override
		protected BaseHolder<CategoryInfo> getHolder() {
			if(!datas.get(position).isTitle()){
				return new CategoryContentHolder();
			}else{
				return new CategoryTitleHolder();
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			this.position = position;
			return super.getView(position, convertView, parent);
		}

		@Override
		protected boolean hasMore() {
			return false;
		}

		@Override
		protected List<CategoryInfo> onload() {
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// 又额外多了一种条目类型 现在又三种 1 标题 2 内容 3 加载更多(没有显示)
			return super.getViewTypeCount() + 1;
		}

		@Override
		protected int getInnerItemViewType(int position) {
			if (datas.get(position).isTitle()) {
				return ITEM_TITLE;
			} else {
				return super.getInnerItemViewType(position);
			}
		}
	}

	@Override
	protected LoadResult load() {
		CategoryProtocol protocol = new CategoryProtocol();
		datas = protocol.load(0);
		return checkData(datas);
	}

}
