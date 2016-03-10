package be.kdg.kandoe.kandoe.adapter;

/**
 * Created by Edward on 09/03/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.kdg.kandoe.kandoe.fragment.CardFragment;
import be.kdg.kandoe.kandoe.fragment.ChatFragment;
import be.kdg.kandoe.kandoe.fragment.GameFragment;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    public static int mNumOfTabs;

    public CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = new ChatFragment();
                return tab1;
            case 1:
                Fragment tab2 = new GameFragment();
                return tab2;
            case 2:
                Fragment tab3 = new CardFragment();
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