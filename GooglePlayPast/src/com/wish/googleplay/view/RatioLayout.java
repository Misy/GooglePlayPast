package com.wish.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioLayout extends FrameLayout {
	// 按照宽高比例去显示
	private float ratio = 2.43f; // 比例值

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	public RatioLayout(Context context) {
		super(context);
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 参数1 命名空间 参数2 属性的名字 参数3 默认的值
		float ratio = attrs.getAttributeFloatValue(
				"http://schemas.android.com/apk/res/com.wish.googleplay",
				"ratio", 2.43f);
		setRatio(ratio);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// widthMeasureSpec 宽度的规则 包含了两部分 (模式和值)，高度规则也是一样的道理
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获得宽度的模式
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);// 获得宽度的尺寸
		// 重新制定宽度大小，即去掉左右两边的padding
		int width = widthSize - getPaddingLeft() - getPaddingRight();

		int heightMode = MeasureSpec.getMode(heightMeasureSpec); // 模式
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);// 高度大小
		int height = heightSize - getPaddingTop() - getPaddingBottom();// 去掉上下两边的padding

		// 宽度模式为精确值，高度不为精确值的情况（一般来说，当在布局文件中定义控件如果是match_parent的话则是精确值，
		// 为wrap_content的话则是最大值）
		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY) {
			// 修正一下高度的值 让高度=宽度/比例
			height = (int) (width / ratio + 0.5f);// 加上0.5是为了保证四舍五入，因为我们强转为int类型时，小数点会被去掉。
		} else if (widthMode != MeasureSpec.EXACTLY
				&& heightMode == MeasureSpec.EXACTLY) {
			// 由于高度是精确的值 ,宽度随着高度的变化而变化
			width = (int) (height * ratio + 0.5f);
		}
		// 重新制作了新的规则(即把上面修正了的宽高再加上各自减去的padding)
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft()
				+ getPaddingRight(), MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height
				+ getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
