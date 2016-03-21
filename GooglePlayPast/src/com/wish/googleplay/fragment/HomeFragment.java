package com.wish.googleplay.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.wish.googleplay.DetailActivity;
import com.wish.googleplay.R;
import com.wish.googleplay.adapter.ListBaseAdapter;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.holder.HomePictureHolder;
import com.wish.googleplay.protocol.HomeProtocol;
import com.wish.googleplay.tools.UiUtils;
import com.wish.googleplay.view.BaseListView;
import com.wish.googleplay.view.LoadingPage.LoadResult;

public class HomeFragment extends BaseFragment {
	private List<AppInfo> datas;
	private List<String> pictures; // 顶部ViewPager 显示界面的数据

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		show();
	}

	public View createSuccessView() {
		BaseListView listView = new BaseListView(UiUtils.getContext());
		HomePictureHolder holder = new HomePictureHolder();
		holder.setData(pictures);
		View contentView = holder.getContentView(); // 得到holder里面管理的view对象
		listView.addHeaderView(contentView); // 把holder里的view对象 添加到listView的上面

		listView.setAdapter(new ListBaseAdapter(datas, listView) {

			@Override
			protected List<AppInfo> onload() {
				HomeProtocol protocol = new HomeProtocol();
				List<AppInfo> load = protocol.load(datas.size());
				datas.addAll(load);
				return load;
			}

		

		});
		listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default); // �������ͼƬ��������ʾ��ͼƬ
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);// ����ʧ����ʾ��ͼƬ

		return listView;
	}

	public LoadResult load() {
		HomeProtocol protocol = new HomeProtocol();
		datas = protocol.load(0);
		pictures = protocol.getPictures();
		return checkData(datas);
	}

}
