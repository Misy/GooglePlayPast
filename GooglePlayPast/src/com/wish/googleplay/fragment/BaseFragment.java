package com.wish.googleplay.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.BitmapUtils;
import com.wish.googleplay.tools.BitmapHelper;
import com.wish.googleplay.tools.ViewUtils;
import com.wish.googleplay.view.LoadingPage;
import com.wish.googleplay.view.LoadingPage.LoadResult;

public abstract class BaseFragment extends Fragment {

	private LoadingPage loadingPage;
	protected BitmapUtils bitmapUtils;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bitmapUtils = BitmapHelper.getBitmapUtils();
		
		if (loadingPage == null) {  // ֮ǰ��frameLayout �Ѿ���¼��һ������  ����֮ǰ��ViewPager 
			loadingPage = new LoadingPage(getActivity()){

				@Override
				public View createSuccessView() {
					return BaseFragment.this.createSuccessView();
				}

				@Override
				protected LoadResult load() {
					return BaseFragment.this.load();
				}
			};
		}else{
			ViewUtils.removeParent(loadingPage);// �Ƴ�frameLayout֮ǰ�ĵ�
		}
	
		return loadingPage;  //  �õ���ǰviewPager ������framelayout  
	}
	/***
	 *  �����ɹ��Ľ���
	 * @return
	 */
	public  abstract View createSuccessView();
	/**
	 * ���������
	 * @return
	 */
	protected abstract LoadResult load();

	public void show(){
		if(loadingPage!=null){
			loadingPage.show();
		}
	}
	
	
	/**У������ */
	public LoadResult checkData(List datas) {
		if(datas==null){
			return LoadResult.error;//  ���������ʧ��
		}else{
			if(datas.size()==0){
				return LoadResult.empty;
			}else{
				return LoadResult.success;
			}
		}
		
	}


}
