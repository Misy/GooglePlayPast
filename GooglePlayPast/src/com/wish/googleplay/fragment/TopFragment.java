package com.wish.googleplay.fragment;

import com.wish.googleplay.view.LoadingPage.LoadResult;

import android.view.View;


public class TopFragment extends BaseFragment {

	@Override
	public View createSuccessView() {
		return null;
	}

	@Override
	protected LoadResult load() {
		return LoadResult.error;
	}
}
