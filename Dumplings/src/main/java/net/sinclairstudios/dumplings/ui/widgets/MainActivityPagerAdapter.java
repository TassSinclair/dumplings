package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.ui.fragment.RatiosMainFragment;
import net.sinclairstudios.dumplings.ui.fragment.RatiosMainFragment_;
import net.sinclairstudios.dumplings.ui.fragment.ServingsMainFragment;
import net.sinclairstudios.dumplings.ui.fragment.ServingsMainFragment_;


public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {

    private final NamedFragment[] fragments;

    public MainActivityPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);

        fragments = new NamedFragment[]{
                new NamedFragment(context.getString(R.string.ratingsAndRatiosLabel), new RatiosMainFragment_()),
                new NamedFragment(context.getString(R.string.specificServingsLabel), new ServingsMainFragment_())
        };
    }


    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position].fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].name;
    }

    private static class NamedFragment {
        private final String name;
        private final Fragment fragment;

        private NamedFragment(String name, Fragment fragment) {
            this.name = name;
            this.fragment = fragment;
        }
    }
}
