package com.wish.googleplay;

import android.annotation.SuppressLint;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wish.googleplay.fragment.BaseFragment;
import com.wish.googleplay.fragment.FragmentFactory;
import com.wish.googleplay.holder.MenuHolder;
import com.wish.googleplay.tools.UiUtils;

public class MainActivity extends BaseActivity implements OnQueryTextListener {
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private ViewPager mViewPager;
	private PagerTabStrip pager_tab_strip;
	private String[] tab_names;
	private FrameLayout fl_menu;

	@Override
	protected void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_am, R.string.open_drawer,
				R.string.close_drawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Toast.makeText(getApplicationContext(), "����ر���", 0).show();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Toast.makeText(getApplicationContext(), "�������", 0).show();
			}

		};
		mDrawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();

	}

	@Override
	protected void init() {
		tab_names = UiUtils.getStringArray(R.array.tab_names);
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl);
		mViewPager = (ViewPager) findViewById(R.id.vp);
		pager_tab_strip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
		fl_menu = (FrameLayout) findViewById(R.id.fl_menu);
		MenuHolder holder = new MenuHolder();
		// holder.setData(data);
		fl_menu.addView(holder.getContentView());
		pager_tab_strip.setTabIndicatorColor(getResources().getColor(
				R.color.indicatorcolor));

		mViewPager.setAdapter(new MainAdpater(getSupportFragmentManager()));
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						super.onPageSelected(position);
						BaseFragment createFragment = FragmentFactory
								.createFragment(position);
						createFragment.show();
					}

				});
		// sendBroadcast(new Intent("com.itheima.google.killall"));
		// Log.i(tag, msg)
	}

	private class MainAdpater extends FragmentStatePagerAdapter {
		public MainAdpater(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.createFragment(position);
		}

		// һ���м�����Ŀ
		@Override
		public int getCount() {
			return tab_names.length;
		}

		// ����ÿ����Ŀ�ı���
		@Override
		public CharSequence getPageTitle(int position) {
			return tab_names[position];
		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		// ������еĻ��� (����ʲô�汾���ֻ� )����3.0
		if (android.os.Build.VERSION.SDK_INT > 11) {
			SearchView searchView = (SearchView) menu.findItem(
					R.id.action_search).getActionView();
			searchView.setOnQueryTextListener(this);// �����ļ���
		}
		return true;
	}

	/** ����actionBar�˵���Ŀ�ĵ���¼� */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_search) {
			Toast.makeText(getApplicationContext(), "����", 0).show();
		}
		return drawerToggle.onOptionsItemSelected(item)
				| super.onOptionsItemSelected(item);
	}

	// �������ύ��ʱ��
	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(getApplicationContext(), query, 0).show();
		return true;
	}

	// ���������ı������仯
	@Override
	public boolean onQueryTextChange(String newText) {
		return true;
	}
}
