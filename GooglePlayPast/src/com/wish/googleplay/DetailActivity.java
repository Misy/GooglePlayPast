package com.wish.googleplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.holder.DetailBottomHolder;
import com.wish.googleplay.holder.DetailDesHolder;
import com.wish.googleplay.holder.DetailInfoHolder;
import com.wish.googleplay.holder.DetailSafeHolder;
import com.wish.googleplay.holder.DetailScreenHolder;
import com.wish.googleplay.protocol.DetailProtocol;
import com.wish.googleplay.tools.UiUtils;
import com.wish.googleplay.view.LoadingPage;
import com.wish.googleplay.view.LoadingPage.LoadResult;

public class DetailActivity extends BaseActivity {
	private String packageName;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		packageName = intent.getStringExtra("packageName");
		System.out.println("packageName==" + packageName);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		LoadingPage loadingPage = new LoadingPage(this) {

			@Override
			protected LoadResult load() {
				return DetailActivity.this.load();
			}

			@Override
			public View createSuccessView() {
				return DetailActivity.this.createSuccessView();
			}
		};
		loadingPage.show();// 必须调用show方法 才会请求服务器 加载新的界面
		setContentView(loadingPage);
		System.out.println("界面设置成功");
	}

	protected LoadResult load() {

		DetailProtocol protocol = new DetailProtocol(packageName);
		data = protocol.load(0);
		if (data == null) {
			return LoadResult.error;
		} else {
			return LoadResult.success;
		}
	}

	private FrameLayout bottom_layout, detail_info, detail_safe, detail_des;
	private HorizontalScrollView detail_screen;
	private DetailInfoHolder detailInfoHolder;
	private DetailScreenHolder detailScreenHolder;
	private DetailSafeHolder safeHolder;
	private DetailDesHolder desHolder;
	private DetailBottomHolder bottomHolder;

	protected View createSuccessView() {
		View view = UiUtils.inflate(R.layout.activity_detail);

		// 添加信息区域
		bottom_layout = (FrameLayout) view.findViewById(R.id.bottom_layout);
		bottomHolder= new DetailBottomHolder();
		bottomHolder.setData(data);
		bottom_layout.addView(bottomHolder.getContentView());
		
		// 操作 应用程序信息
		detail_info = (FrameLayout) view.findViewById(R.id.detail_info);
		detailInfoHolder = new DetailInfoHolder();
		detailInfoHolder.setData(data);
		detail_info.addView(detailInfoHolder.getContentView());

		// 安全标记
		detail_safe = (FrameLayout) view.findViewById(R.id.detail_safe);
		safeHolder = new DetailSafeHolder();
		safeHolder.setData(data);
		detail_safe.addView(safeHolder.getContentView());

		// 中间的5张图片（有的可能没有5张那么多）
		detail_screen = (HorizontalScrollView) view
				.findViewById(R.id.detail_screen);
		detailScreenHolder = new DetailScreenHolder();
		detailScreenHolder.setData(data);
		detail_screen.addView(detailScreenHolder.getContentView());

		//应用简介描述holder
		detail_des = (FrameLayout) view.findViewById(R.id.detail_des);
		desHolder = new DetailDesHolder();
		desHolder.setData(data);
		detail_des.addView(desHolder.getContentView());

		return view;
	}

	@Override
	protected void initActionBar() {
		super.initActionBar();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
