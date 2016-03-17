package be.kdg.kandoe.kandoe.adapter;

/**
 * Created by Edward on 09/03/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import be.kdg.kandoe.kandoe.fragment.CardFragment;
import be.kdg.kandoe.kandoe.fragment.GameFragment;
import be.kdg.kandoe.kandoe.activity.OrganisationActivity;
import be.kdg.kandoe.kandoe.fragment.ThemeCardFragment;

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
                return new GameFragment();
            case 1:
                return new CardFragment();
            case 2:
                return new ThemeCardFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}