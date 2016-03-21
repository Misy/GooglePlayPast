package com.wish.googleplay.tools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.wish.googleplay.BaseActivity;
import com.wish.googleplay.BaseApplication;

public class UiUtils {
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return BaseApplication.getApplication().getResources();
	}

	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	public static int dip2px(int dip) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	public static int px2dip(int px) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static void runOnUiThread(Runnable runnable) {
		if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
			runnable.run();
		} else {
			BaseApplication.getHandler().post(runnable);
		}
	}

	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	public static Drawable getDrawalbe(int id) {
		return getResource().getDrawable(id);
	}

	public static int getDimens(int homePictureHeight) {
		return (int) getResource().getDimension(homePictureHeight);
	}

	/**
	 * 延迟执行 任务
	 * 
	 * @param run
	 *            任务
	 * @param time
	 *            延迟的时间
	 */
	public static void postDelayed(Runnable run, int time) {
		BaseApplication.getHandler().postDelayed(run, time); // 调用Runable里面的run方法
	}

	/**
	 * 取消任务
	 * 
	 * @param auToRunTask
	 */
	public static void cancel(Runnable auToRunTask) {
		BaseApplication.getHandler().removeCallbacks(auToRunTask);
	}

	public static void startActivity(Intent intent) {
		// 如果不在activity里去打开activity 需要指定任务栈 需要设置标签
		if (BaseActivity.activity == null) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		} else {
			// 注意这里不能拿getContext去调用startActivity方法，因为getContext里默认也是没activity任务栈的
			BaseActivity.activity.startActivity(intent);
		}
	}
}
