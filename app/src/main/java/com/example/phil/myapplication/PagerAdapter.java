package com.example.phil.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter
{
    int num;

    public PagerAdapter(FragmentManager fm, int num)
    {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 :
                Blacklist blacklist = new Blacklist();
                return blacklist;
            case 1 :
                Message message = new Message();
                return message;
            case 2 :
                Schedule schedule = new Schedule();
                return schedule;
            default: return null;
        }

    }

    @Override
    public int getCount()
    {
        return num;
    }
}
