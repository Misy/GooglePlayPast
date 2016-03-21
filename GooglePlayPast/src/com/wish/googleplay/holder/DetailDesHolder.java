package com.wish.googleplay.holder;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.wish.googleplay.R;
import com.wish.googleplay.domain.AppInfo;
import com.wish.googleplay.tools.UiUtils;

public class DetailDesHolder extends BaseHolder<AppInfo> implements
		OnClickListener {
	@ViewInject(R.id.des_content)
	private TextView des_content;
	@ViewInject(R.id.des_author)
	private TextView des_author;
	@ViewInject(R.id.des_arrow)
	private ImageView des_arrow;
	@ViewInject(R.id.des_layout)
	private RelativeLayout des_layout;

	@Override
	public View initView() {
		View view = UiUtils.inflate(R.layout.detail_des);
		ViewUtils.inject(this, view);
		// 初始化des_content显示的高度
		LayoutParams layoutParams = des_content.getLayoutParams();
		layoutParams.height = getShortMeasureHeight();
		des_content.setLayoutParams(layoutParams);
		// 箭头初始化显示状态
		des_arrow.setImageResource(R.drawable.arrow_down);
		return view;
	}

	/**
	 * 获取简介信息（textview）最开始高度， 起始高度我们设置7行的高度（也就是折叠之后没展开的要显示的高度）
	 * 
	 * @return
	 */
	private int getShortMeasureHeight() {
		// 复制一个新的TextView 用来测量,因为在之前的TextView测量 有可能影响其它测量代码，导致测量结果不准
		TextView tv = new TextView(UiUtils.getContext());
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);// 设置字体大小为14dp
		tv.setMaxLines(7);// 设置显示的最大行数
		tv.setLines(7);// 这是因为如果没内容的话，即使设置了最大行数为7行也没效果，还需要设置该参数让行数强制为7行，即使textview中没任何内容

		int width = des_content.getMeasuredWidth();
		// des_content.getLayoutParams().height =
		// ViewGroup.LayoutParams.WRAP_CONTENT;// 设置高度为包裹内容，该参数不设置其实也可以。
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
				MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000,
				MeasureSpec.AT_MOST);// 最大高度为1000，实际以测量为准
		tv.measure(widthMeasureSpec, heightMeasureSpec);

		return tv.getMeasuredHeight();
	}

	/**
	 * 获取TextView 自己本身的高度,即展开之后的高度
	 * 
	 * @return
	 */
	private int getLongMeasureHeight() {
		int width = des_content.getMeasuredWidth(); // 开始宽度
		des_content.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;// 高度包裹内容

		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
				width);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.AT_MOST, 1000);
		des_content.measure(widthMeasureSpec, heightMeasureSpec);
		return des_content.getMeasuredHeight();
	}

	@Override
	public void refreshView(AppInfo data) {
		des_content.setText(data.getDes());
		des_author.setText("作者:" + data.getAuthor());
		des_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		expand();
	}

	boolean flag;
	ScrollView scrollView;

	private void expand() {
		int startHeight;
		int targetHeight;
		scrollView = getScrollView(des_layout);
		if (!flag) {
			flag = true;
			startHeight = getShortMeasureHeight();
			targetHeight = getLongMeasureHeight();
		} else {
			flag = false;
			startHeight = getLongMeasureHeight();
			targetHeight = getShortMeasureHeight();
		}
		final LayoutParams layoutParams = des_content.getLayoutParams();
		ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);

		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int value = (Integer) animation.getAnimatedValue();
				layoutParams.height = value;
				des_content.setLayoutParams(layoutParams);
				scrollView.scrollTo(0, scrollView.getMeasuredHeight());
			}
		});
		animator.addListener(new AnimatorListener() { // 监听动画执行
			// 当动画开始执行的时候调用
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				if (flag) {
					des_arrow.setImageResource(R.drawable.arrow_up);
				} else {
					des_arrow.setImageResource(R.drawable.arrow_down);
				}
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		animator.setDuration(500);// 设置动画持续时间
		animator.start();
	}

	/**
	 * 获取到界面的ScollView
	 */
	private ScrollView getScrollView(View view) {
		ViewParent parent = view.getParent();
		if (parent instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) parent;
			if (group instanceof ScrollView) {
				return (ScrollView) group;
			} else {
				return getScrollView(group);// 递归调用该方法继续获取scrollview。
			}
		} else {
			return null;
		}
	}
}
