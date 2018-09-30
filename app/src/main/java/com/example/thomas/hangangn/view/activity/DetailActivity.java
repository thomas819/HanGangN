package com.example.thomas.hangangn.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.view.fragment.detail.ParkEnjoyFragment;
import com.example.thomas.hangangn.view.fragment.detail.ParkInfoFragment;
import com.example.thomas.hangangn.view.fragment.detail.ParkParkingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.activity_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_detail_tv)
    TextView mTv;
    @BindView(R.id.activity_detail_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.activity_detail_viewpager)
    ViewPager mViewpager;

    String name;
    int img;
    String[] urlList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initViews();
        initViewpager();

    }

    private void initViews() {
        Intent intent = getIntent();
        name = intent.getStringExtra("placeName");
        img = intent.getIntExtra("placeImg", 0);
        urlList= intent.getStringArrayExtra("placeUrl");
        mToolbar.setTitle(name + "한강공원");
        //mToolbar.setBackgroundColor(getResources().getColor());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        System.out.println("detailActivity"+urlList[0]+","+urlList[1]+","+urlList[2]);

        mTv.setText(name + "한강공원");
        mTv.setBackgroundResource(img);
    }

    private void initViewpager() {
        mTabLayout.addTab(mTabLayout.newTab().setText("공원정보"));
        mTabLayout.addTab(mTabLayout.newTab().setText("즐길거리"));
        mTabLayout.addTab(mTabLayout.newTab().setText("주차정보"));

        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ParkInfoFragment.newInstance(urlList[0]);
                    case 1:
                        return ParkEnjoyFragment.newInstance(urlList[1]);
                    case 2:
                        return ParkParkingFragment.newInstance(urlList[2]);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id ==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
