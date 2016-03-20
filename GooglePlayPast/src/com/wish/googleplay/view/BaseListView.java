package com.wish.googleplay.view;

import com.wish.googleplay.R;
import com.wish.googleplay.tools.UiUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BaseListView extends ListView {
	
	public BaseListView(Context context) {
		super(context);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		this.setSelector(R.drawable.nothing);  // ʲô��û��
		this.setCacheColorHint(R.drawable.nothing);
		this.setDivider(UiUtils.getDrawalbe(R.drawable.nothing));
	}

}
