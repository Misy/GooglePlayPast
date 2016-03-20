package com.wish.googleplay.holder;

import java.util.List;

import android.view.View;
import android.widget.ImageView;

import com.wish.googleplay.R;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.tools.UiUtils;

public class DetailScreenHolder extends BaseHolder<AppInfo> {
	private ImageView[] ivs;

	@Override
	public View initView() {
		View view = UiUtils.inflate(R.layout.detail_screen);
		ivs = new ImageView[5];
		ivs[0] = (ImageView) view.findViewById(R.id.screen_1);
		ivs[1] = (ImageView) view.findViewById(R.id.screen_2);
		ivs[2] = (ImageView) view.findViewById(R.id.screen_3);
		ivs[3] = (ImageView) view.findViewById(R.id.screen_4);
		ivs[4] = (ImageView) view.findViewById(R.id.screen_5);
		return view;
	}

	@Override
	public void refreshView(AppInfo data) {
		List<String> screens = data.getScreen();// 集合的大小有可能比5小（因为有的应用显示的图片没有5张那么多）
		for (int i = 0; i < 5; i++) {
			if (i < screens.size()) {
				// i小于图片的长度才去显示图片，否则不显示图片
				ivs[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(ivs[i], HttpHelper.URL + "image?name="
						+ screens.get(i));
			} else {
				ivs[i].setVisibility(View.GONE);
			}
		}
	}

}
