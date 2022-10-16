package com.rekortech.android;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class PagerViewAdapter extends FragmentPagerAdapter{

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment =new lab_home_fragment();
                break;
            case 1:
                fragment = new lab_home_fragment();
                break;
            case 2:
                fragment = new lab_home_fragment();
                break;
            case 3:
                fragment = new lab_home_fragment();
                break;
            default:
                fragment = new lab_home_fragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
