package net.sinclairstudios.android.dumplings.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import net.sinclairstudios.android.dumplings.activity.HowManyPeopleFragment_;

public class MainPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int item) {
        return MainPagerFragment.values()[item].fragment;
    }

    @Override
    public int getCount() {
        return MainPagerFragment.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MainPagerFragment.values()[position].name;
    }

    private static enum MainPagerFragment {

        RECOMMENDATIONS ("Recommendations", new HowManyPeopleFragment_()),
        LET_ME_CHOOSE ("Let me choose", new HowManyPeopleFragment_()); // This will become something else soon.

        private final String name;
        private final Fragment fragment;

        MainPagerFragment(String name, Fragment fragment) {
            this.name = name;
            this.fragment = fragment;
        }
    }
}
