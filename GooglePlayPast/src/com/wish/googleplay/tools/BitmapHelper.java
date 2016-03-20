package com.wish.googleplay.tools;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
	private BitmapHelper() {
	}

	private static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils���ǵ����� ������Ҫ���ض����ȡʵ���ķ���
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getBitmapUtils() {
		if (bitmapUtils == null) {
			// �ڶ������� ����ͼƬ��·�� // ����ͼƬ ������Ķ��ٱ������ڴ� 0.05-0.8f
			bitmapUtils = new BitmapUtils(UiUtils.getContext(), FileUtils
					.getIconDir().getAbsolutePath(), 0.3f);
		}
		return bitmapUtils;
	}
}
