package com.example.thomas.hangangn.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.view.fragment.FavoritesFragment;
import com.example.thomas.hangangn.view.fragment.HomeFragment;
import com.example.thomas.hangangn.view.fragment.MapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_viewpager)
    ViewPager mViewpager;
    @BindView(R.id.activity_main_bottom_navi)
    BottomNavigationView mBottomNavi;
    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getLayoutInflater().inflate(R.layout.layout_custom_toolbar,mToolbar);
        //mToolbar.addView(view);
        setSupportActionBar(mToolbar);

        initView();
    }

    private void initView() {
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new FavoritesFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavi.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi1:
                        mViewpager.setCurrentItem(0);
                        return true;
                    case R.id.navi2:
                        mViewpager.setCurrentItem(1);
                        return true;
                    case R.id.navi3:
                        mViewpager.setCurrentItem(2);
                        return true;
                }
                return true;
            }
        });
    }
}
