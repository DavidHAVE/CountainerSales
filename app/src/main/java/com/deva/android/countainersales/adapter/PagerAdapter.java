package com.deva.android.countainersales.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.deva.android.countainersales.fragment.AccountFragment;
import com.deva.android.countainersales.fragment.IntroFragment;
import com.deva.android.countainersales.fragment.OrderFragment;

/**
 * Created by David on 05/10/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                IntroFragment tab1 = new IntroFragment();
                return tab1;
            case 1:
                OrderFragment tab2 = new OrderFragment();
                return tab2;
            case 2:
                AccountFragment tab3 = new AccountFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
