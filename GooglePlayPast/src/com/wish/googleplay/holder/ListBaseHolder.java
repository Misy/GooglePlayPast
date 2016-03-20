package com.wish.googleplay.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wish.googleplay.R;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.tools.UiUtils;

public class ListBaseHolder extends BaseHolder<AppInfo> {
	ImageView item_icon;
	TextView item_title, item_size, item_bottom;
	RatingBar item_rating;

	public void refreshView(AppInfo data) {
		this.item_title.setText(data.getName());// 设置应用程序的名字
		String appSize = Formatter.formatFileSize(UiUtils.getContext(),
				data.getSize());
		this.item_size.setText(appSize);// 设置应用程序的大小
		this.item_bottom.setText(data.getDes());// 设置应用程序的描述信息
		this.item_rating.setRating((float) data.getStars());// 设置ratingBar的值(评分)
		String iconUrl = data.getIconUrl();// 得到图片对应的地址（app/com.youyuan.yyhl/icon.jpg）
		bitmapUtils.display(this.item_icon, HttpHelper.URL + "image?name="
				+ iconUrl);
	}

	@Override
	public View initView() {
		View contentView = UiUtils.inflate(R.layout.item_app);
		this.item_icon = (ImageView) contentView.findViewById(R.id.item_icon);
		this.item_title = (TextView) contentView.findViewById(R.id.item_title);
		this.item_size = (TextView) contentView.findViewById(R.id.item_size);
		this.item_bottom = (TextView) contentView
				.findViewById(R.id.item_bottom);
		this.item_rating = (RatingBar) contentView
				.findViewById(R.id.item_rating);
		return contentView;
	}

}
