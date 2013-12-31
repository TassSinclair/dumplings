package net.sinclairstudios.android.dumplings.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.android.dumplings.layout.MainPagerAdapter;

@EActivity(R.layout.main_layout)
public class MainActivity extends FragmentActivity {

    FragmentPagerAdapter fragmentPagerAdapter;

    @ViewById(R.id.pager)
    ViewPager viewPager;

    @AfterViews
    public void setupViewPager() {
        fragmentPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutMenuItem:
                startActivity(new Intent(this, AboutActivity_.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
