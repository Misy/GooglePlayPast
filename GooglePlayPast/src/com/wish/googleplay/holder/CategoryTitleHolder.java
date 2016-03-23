package com.wish.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wish.googleplay.R;
import com.wish.googleplay.domain.CategoryInfo;
import com.wish.googleplay.tools.UiUtils;

public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {
	private TextView tv;

	@Override
	public View initView() {
		tv = new TextView(UiUtils.getContext());
		tv.setTextColor(Color.BLACK);
		tv.setBackgroundDrawable(UiUtils.getDrawalbe(R.drawable.grid_item_bg));
		return tv;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tv.setText(data.getTitle());
	}
}
