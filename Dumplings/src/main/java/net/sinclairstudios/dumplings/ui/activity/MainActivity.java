package net.sinclairstudios.dumplings.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.ui.widgets.MainActivityPagerAdapter;

@EActivity(R.layout.main_layout)
public class MainActivity extends FragmentActivity {

    private final static String TAB_CHOICE_KEY = "TAB_CHOICE";

    private MainActivityPagerAdapter viewPagerAdapter;
    private int tabChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabChoice = getPreferences().getInt(TAB_CHOICE_KEY, 0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferences().edit()
            .putInt(TAB_CHOICE_KEY, tabChoice)
            .commit();

    }

    @ViewById
    ViewPager pager;

    @AfterViews
    protected void setUpAdpater() {
        viewPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(viewPagerAdapter);
        pager.setCurrentItem(tabChoice);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                tabChoice = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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

    private SharedPreferences getPreferences() {
        return getPreferences(Activity.MODE_PRIVATE);
    }

}
