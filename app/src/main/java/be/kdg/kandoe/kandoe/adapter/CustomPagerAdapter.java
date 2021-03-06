package be.kdg.kandoe.kandoe.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.kdg.kandoe.kandoe.fragment.CardFragment;
import be.kdg.kandoe.kandoe.fragment.ChatFragment;
import be.kdg.kandoe.kandoe.fragment.GameFragment;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    public int mNumOfTabs;

    public CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new GameFragment();
            case 2:
                return new CardFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}