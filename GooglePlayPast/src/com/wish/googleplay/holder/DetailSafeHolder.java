package com.wish.googleplay.holder;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.wish.googleplay.R;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.http.HttpHelper;
import com.wish.googleplay.tools.UiUtils;

public class DetailSafeHolder extends BaseHolder<AppInfo> implements
		OnClickListener {
	@ViewInject(R.id.safe_layout)
	private RelativeLayout safe_layout;
	@ViewInject(R.id.safe_content)
	private LinearLayout safe_content;
	@ViewInject(R.id.safe_arrow)
	private ImageView safe_arrow;
	ImageView[] ivs;
	ImageView[] iv_des;
	TextView[] tv_des;
	LinearLayout[] des_layout;

	@Override
	public View initView() {
		View view = UiUtils.inflate(R.layout.detail_safe);
		ViewUtils.inject(this, view);

		ivs = new ImageView[4]; // 初始化标题栏的图片
		ivs[0] = (ImageView) view.findViewById(R.id.iv_1);
		ivs[1] = (ImageView) view.findViewById(R.id.iv_2);
		ivs[2] = (ImageView) view.findViewById(R.id.iv_3);
		ivs[3] = (ImageView) view.findViewById(R.id.iv_4);
		iv_des = new ImageView[4]; // 初始化每个条目描述的图片
		iv_des[0] = (ImageView) view.findViewById(R.id.des_iv_1);
		iv_des[1] = (ImageView) view.findViewById(R.id.des_iv_2);
		iv_des[2] = (ImageView) view.findViewById(R.id.des_iv_3);
		iv_des[3] = (ImageView) view.findViewById(R.id.des_iv_4);
		tv_des = new TextView[4]; // 初始化每个条目描述的文本
		tv_des[0] = (TextView) view.findViewById(R.id.des_tv_1);
		tv_des[1] = (TextView) view.findViewById(R.id.des_tv_2);
		tv_des[2] = (TextView) view.findViewById(R.id.des_tv_3);
		tv_des[3] = (TextView) view.findViewById(R.id.des_tv_4);

		des_layout = new LinearLayout[4]; // 初始化条目线性布局
		des_layout[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
		des_layout[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
		des_layout[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
		des_layout[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);

		// 让初始化状态安全标记界面为隐藏
		LayoutParams layoutParams = safe_content.getLayoutParams();
		layoutParams.height = 0;
		safe_content.setLayoutParams(layoutParams);

		safe_arrow.setImageResource(R.drawable.arrow_down);
		return view;

	}

	@Override
	public void refreshView(AppInfo data) {
		safe_layout.setOnClickListener(this);

		List<String> safeUrl = data.getSafeUrl();
		List<String> safeDesUrl = data.getSafeDesUrl();
		List<String> safeDes = data.getSafeDes();
		List<Integer> safeDesColor = data.getSafeDesColor(); // 0 1 2 3

		for (int i = 0; i < 4; i++) {
			if (i < safeUrl.size() && i < safeDesUrl.size()
					&& i < safeDes.size() && i < safeDesColor.size()) {
				ivs[i].setVisibility(View.VISIBLE);
				des_layout[i].setVisibility(View.VISIBLE);
				bitmapUtils.display(ivs[i], HttpHelper.URL + "image?name="
						+ safeUrl.get(i));
				bitmapUtils.display(iv_des[i], HttpHelper.URL + "image?name="
						+ safeDesUrl.get(i));
				tv_des[i].setText(safeDes.get(i));
				// 根据服务器数据显示不同的颜色
				int color;
				int colorType = safeDesColor.get(i);
				if (colorType >= 1 && colorType <= 3) {
					color = Color.rgb(255, 153, 0); // 00 00 00
				} else if (colorType == 4) {
					color = Color.rgb(0, 177, 62);
				} else {
					color = Color.rgb(122, 122, 122);
				}
				tv_des[i].setTextColor(color);
			} else {
				ivs[i].setVisibility(View.GONE);
				des_layout[i].setVisibility(View.GONE);
			}
		}
	}

	boolean flag = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safe_layout:
			int startHeight,
			targetHeight;// 值动画要用到的两个起始位置
			if (!flag) { // 隐藏状态，需要做展开的动画，值从0变化到目标高度
				startHeight = 0;
				targetHeight = getMeasureHeight();
				flag = true;
				// safe_content.setVisibility(View.VISIBLE);
			} else {
				startHeight = getMeasureHeight();
				targetHeight = 0;
				flag = false;
				// safe_content.setVisibility(View.GONE);
			}

			ValueAnimator animator = ValueAnimator.ofInt(startHeight,
					targetHeight);
			final LayoutParams layoutParams = safe_content.getLayoutParams();
			// 监听值的变化
			animator.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					int value = (Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
					layoutParams.height = value;
					safe_content.setLayoutParams(layoutParams);// 刷新界面
				}
			});

			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator arg0) {

				}

				@Override
				public void onAnimationRepeat(Animator arg0) {

				}

				@Override
				public void onAnimationEnd(Animator arg0) {
					// 动画运行结束判断布局是显示或隐藏，从而进行相应操作
					if (flag) {
						safe_arrow.setImageResource(R.drawable.arrow_up);
					} else {
						safe_arrow.setImageResource(R.drawable.arrow_down);
					}
				}

				@Override
				public void onAnimationCancel(Animator arg0) {
					// TODO Auto-generated method stub

				}
			});
			animator.setDuration(500);
			animator.start();
			break;

		}
	}

	private int getMeasureHeight() {
		int width = safe_content.getMeasuredWidth();// 由于宽度是固定不变的，我们可以直接取出来
		safe_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;// 高度是不确定的，我们让它包裹内容

		// 参数1：尺寸。
		// 参数2：测量控件的模式（EXACTLY--精确测量，也就是传入的值就是实际值。AT_MOST--最大值，传入的值为可以变化的最大值，但是以实际值为准，比如我们上面定义了高度为包裹内容）
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000,
				MeasureSpec.AT_MOST);

		safe_content.measure(widthMeasureSpec, heightMeasureSpec);// 通过该方法重新测量控件
		return safe_content.getMeasuredHeight();
	}
}
