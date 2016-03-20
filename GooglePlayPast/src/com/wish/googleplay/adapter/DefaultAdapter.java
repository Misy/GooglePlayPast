package com.wish.googleplay.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wish.googleplay.holder.BaseHolder;
import com.wish.googleplay.holder.MoreHolder;
import com.wish.googleplay.manager.ThreadManager;
import com.wish.googleplay.tools.UiUtils;

public abstract class DefaultAdapter<Data> extends BaseAdapter implements
		OnItemClickListener {
	protected List<Data> datas;
	// 定义两个常量来标识是默认条目，还是加载更多条目
	private static final int DEFAULT_ITEM = 0;
	private static final int MORE_ITEM = 1;
	private ListView listView;

	public DefaultAdapter(List<Data> datas, ListView listView) {
		this.datas = datas;
		listView.setOnItemClickListener(this);
		this.listView = listView;
	}

	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder = null;

		switch (getItemViewType(position)) { // 判断当前条目时什么类型
		case MORE_ITEM:
			if (convertView == null) {
				holder = getMoreHolder();
			} else {
				holder = (BaseHolder) convertView.getTag();
			}
			break;
		case DEFAULT_ITEM:
			if (convertView == null) {
				holder = getHolder();
			} else {
				holder = (BaseHolder) convertView.getTag();
			}
			if (position < datas.size()) {
				holder.setData(datas.get(position));
			}
			break;
		}
		return holder.getContentView(); // 如果当前Holder 恰好是MoreHolder
										// 证明MoreHOlder已经显示
	}

	private MoreHolder holder;

	private BaseHolder getMoreHolder() {
		if (holder != null) {
			return holder;
		} else {
			holder = new MoreHolder(this);
			return holder;
		}
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == datas.size()) {
			return MORE_ITEM;
		}
		return getInnerItemViewType(position);
	}

	private int getInnerItemViewType(int position) {
		return DEFAULT_ITEM;
	}

	protected abstract BaseHolder<Data> getHolder();

	public void loadMore() {
		// 请求网络要放到子线程中执行，调用我们之前创建的线程池类来管理
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {

			@Override
			public void run() {
				// 在子线程中加载更多
				final List<Data> newData = onload();
				UiUtils.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (newData == null) {
							holder.setData(MoreHolder.LOAD_ERROR);//
						} else if (newData.size() == 0) {
							holder.setData(MoreHolder.HAS_NO_MORE);
						} else {
							// 成功了
							holder.setData(MoreHolder.HAS_MORE);
							datas.addAll(newData);// 给listView之前的集合添加一个新的集合

							notifyDataSetChanged();// 刷新界面

						}

					}
				});

			}
		});
	}

	protected abstract List<Data> onload();

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - listView.getHeaderViewsCount();
		onInnerItemClick(position);
	}

	public void onInnerItemClick(int position) {
		
	}
}
