package com.wish.googleplay.fragment;


import com.wish.googleplay.view.LoadingPage.LoadResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryFragment extends BaseFragment {

	@Override
	public View createSuccessView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LoadResult load() {
		return LoadResult.error;
	}
}
