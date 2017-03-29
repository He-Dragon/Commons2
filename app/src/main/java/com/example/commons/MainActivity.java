package com.example.commons;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.RadioGroup;

import com.example.commons.base.BaseActivity;
import com.example.commons.base.BaseFragmentActivity;
import com.example.commons.fragment.HomeFragment;
import com.example.commons.fragment.PersonFragment;
import com.example.commons.utils.CustomDialog;
import com.example.commons.view.BarEntity;
import com.example.commons.view.BottomTabBar;
import com.example.commons.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements BottomTabBar.OnSelectListener {

    private BottomTabBar bottomTabBar;
    private List<BarEntity> bars ;
    private HomeFragment homeFragment ;
    private PersonFragment personFragment ;
    private HomeFragment homeFragment1 ;
    private PersonFragment personFragment1 ;
    private FragmentManager manager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        bars = new ArrayList<>();
        bottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
        manager = getSupportFragmentManager();
        bottomTabBar.setManager(manager);
        bottomTabBar.setOnSelectListener(this);
        bars.add(new BarEntity("主页",R.mipmap.ic_home_select,R.mipmap.ic_home_unselect));
        bars.add(new BarEntity("趣图",R.mipmap.ic_imagejoke_select,R.mipmap.ic_imagejoke_unselect));
        bars.add(new BarEntity("圈子",R.mipmap.ic_dt_select,R.mipmap.ic_dt_unselect));
        bars.add(new BarEntity("个人",R.mipmap.ic_my_select,R.mipmap.ic_my_unselect));
        bottomTabBar.setBars(bars);

    }

    @Override
    public void setViewData() {
    }

    @Override
    public int getContentViewId() {
        return R.layout.ac_main;
    }


    @Override
    public void onSelect(int position) {
        switch (position) {
            case 0:
                if (homeFragment == null){
                    homeFragment = HomeFragment.newInstance();
                }
                bottomTabBar.switchContent(homeFragment);
                break;
            case 1:
                if (personFragment == null){
                    personFragment = PersonFragment.newInstance();
                }
                bottomTabBar.switchContent(personFragment);
                break;
            case 2:
                if (homeFragment1 == null){
                    homeFragment1 = HomeFragment.newInstance();
                }
                bottomTabBar.switchContent(homeFragment1);
                break;
            case 3:
                if (personFragment1 == null){
                    personFragment1 = PersonFragment.newInstance();
                }
                bottomTabBar.switchContent(personFragment1);
                break;
        }
    }

}
