package com.wish.googleplay.holder;

import android.view.View;
import android.widget.RelativeLayout;

import com.wish.googleplay.R;
import com.wish.googleplay.adapter.DefaultAdapter;
import com.wish.googleplay.tools.UiUtils;

public class MoreHolder extends BaseHolder<Integer> {

	public static final int HAS_NO_MORE = 0;// 没有额外数据
	public static final int LOAD_ERROR = 1;// 加载失败
	public static final int HAS_MORE = 2;// 有额外数据
	private DefaultAdapter adapter;
	// 加载更多中布局，加载更多失败布局
	private RelativeLayout rl_more_loading, rl_more_error;

	public MoreHolder(DefaultAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	public View getContentView() {
		loadMore();
		return super.getContentView();
	}

	private void loadMore() {
		// 由于请求网络返回的数据要保存到datas集合中，我们的MoreHolder里面没有这个变量，
		// 所以我们可以把它交给DefaultAdapter去做
		adapter.loadMore();
	}

	@Override
	public View initView() {
		View view = UiUtils.inflate(R.layout.load_more);
		rl_more_loading = (RelativeLayout) view
				.findViewById(R.id.rl_more_loading);
		rl_more_error = (RelativeLayout) view.findViewById(R.id.rl_more_error);
		return view;
	}

	@Override
	public void refreshView(Integer data) {
		// 根据MoreHolder里定义的状态值来判断，如果处于HAS_MORE状态，则显示加载中界面，否则隐藏
		// 同理，如果处于LOAD_ERROR状态，则显示加载失败界面，否则隐藏
		rl_more_error.setVisibility(data == LOAD_ERROR ? View.VISIBLE
				: View.GONE);
		rl_more_loading.setVisibility(data == HAS_MORE ? View.VISIBLE
				: View.GONE);
	}

}
