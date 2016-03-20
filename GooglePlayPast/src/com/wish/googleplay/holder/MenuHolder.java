package com.wish.googleplay.holder;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wish.googleplay.R;
import com.wish.googleplay.domain.UserInfo;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.manager.ThreadManager;
import com.wish.googleplay.manager.ThreadManager.ThreadPoolProxy;
import com.wish.googleplay.protocol.UserProtocol;
import com.wish.googleplay.tools.UiUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuHolder extends BaseHolder<UserInfo> implements OnClickListener {
	@ViewInject(R.id.photo_layout)
	private RelativeLayout photo_layout;
	@ViewInject(R.id.image_photo)
	private ImageView image_photo;
	@ViewInject(R.id.user_name)
	private TextView user_name;
	@ViewInject(R.id.user_email)
	private TextView user_email;

	@Override
	public View initView() {
		View view = UiUtils.inflate(R.layout.menu_holder);
		ViewUtils.inject(this, view);
		photo_layout.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView(UserInfo data) {
		user_name.setText(data.getName());
		user_email.setText(data.getEmail());
		String url = data.getUrl();// -->image/user.png
		bitmapUtils.display(image_photo, HttpHelper.URL + "image?name=" + url);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.photo_layout:
			ThreadManager.getInstance().createLongPool()
					.execute(new Runnable() {

						@Override
						public void run() {
							UserProtocol protocol = new UserProtocol();
							final UserInfo load = protocol.load(0);
							UiUtils.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									setData(load);
								}
							});
						}
					});
			break;

		default:
			break;
		}
	}

}
