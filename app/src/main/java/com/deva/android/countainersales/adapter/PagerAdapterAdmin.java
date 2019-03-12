package com.deva.android.countainersales.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.deva.android.countainersales.fragment.AccountFragment;
import com.deva.android.countainersales.fragment.AdminAccountFragment;
import com.deva.android.countainersales.fragment.BuyerFragment;
import com.deva.android.countainersales.fragment.IntroFragment;
import com.deva.android.countainersales.fragment.OrderFragment;

/**
 * Created by David on 07/10/2017.
 */

public class PagerAdapterAdmin extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapterAdmin(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                BuyerFragment tab1 = new BuyerFragment();
                return tab1;
            case 1:
                AdminAccountFragment tab2 = new AdminAccountFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
