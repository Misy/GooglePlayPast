package com.wish.googleplay.holder;

import java.util.LinkedList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;

import com.wish.googleplay.R;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.tools.UiUtils;

public class HomePictureHolder extends BaseHolder<List<String>> {
	/* 当new HomePictureHolder()就会调用该方法 */
	private ViewPager viewPager;
	private List<String> datas;
	private AutoRunTask task;

	@Override
	public View initView() {
		viewPager = new ViewPager(UiUtils.getContext());
		viewPager.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, UiUtils
						.getDimens(R.dimen.home_picture_height)));
		return viewPager;
	}

	/* 当 holder.setData 才会调用 */
	@Override
	public void refreshView(List<String> datas) {
		this.datas = datas;
		viewPager.setAdapter(new HomeAdapter());
		viewPager.setCurrentItem(2000 * datas.size());
		task = new AutoRunTask();
		task.start();
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					task.stop();
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					task.start();
					break;
				}
				return false;
			}
		});

		// viewPager.setLayoutParams(new
		// FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}

	private boolean flag;

	private class AutoRunTask implements Runnable {
		@Override
		public void run() {
			if (flag) {
				// 开始执行之前先取消上一次任务
				UiUtils.cancel(this);
				int currentItem = viewPager.getCurrentItem();
				currentItem++;
				viewPager.setCurrentItem(currentItem);
				// 延迟执行当前的任务
				UiUtils.postDelayed(this, 2000);
			}
		}

		/**
		 * 开始轮询
		 */
		public void start() {
			// 如果当前没有处于轮询状态，我们才去开始
			if (!flag) {
				UiUtils.cancel(this);
				flag = true;
				UiUtils.postDelayed(this, 2000);
			}
		}

		/**
		 * 停止轮询
		 */
		public void stop() {
			if (flag) {
				flag = false;
				UiUtils.cancel(this);
			}
		}
	}

	class HomeAdapter extends PagerAdapter {
		LinkedList<ImageView> convertView = new LinkedList<ImageView>();

		// 当前viewPager里面有多少个条目
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/* 判断返回的对象和 加载view对象的关系 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);

			ImageView imageView = (ImageView) object;
			convertView.add(imageView);
			container.removeView(imageView);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % datas.size();
			ImageView imageView;
			if (convertView.size() > 0) {
				imageView = convertView.remove(0);
			} else {
				imageView = new ImageView(UiUtils.getContext());
			}
			bitmapUtils.display(imageView, HttpHelper.URL + "image?name="
					+ datas.get(position));
			container.addView(imageView); // 加载的view对象
			return imageView; // 返回的对象
		}

	}
}