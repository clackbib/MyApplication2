package com.example.phil.myapplication.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.phil.myapplication.ui.fragment.BlackListFragment;
import com.example.phil.myapplication.ui.fragment.MessageFragment;
import com.example.phil.myapplication.ui.fragment.RulesFragment;


public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private int num;

    public MainPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BlackListFragment();
            case 1:
                return new MessageFragment();
            case 2:
                return new RulesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
