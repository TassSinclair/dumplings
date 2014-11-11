package net.sinclairstudios.dumplings.ui.activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.example.android.swipedismiss.SwipeDismissListViewTouchListener;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;

import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.*;
import net.sinclairstudios.dumplings.ui.widgets.DismissArrayAdapterItemListViewTouchListener;
import net.sinclairstudios.dumplings.ui.widgets.DumplingRatingAdapter;
import net.sinclairstudios.dumplings.ui.widgets.DumplingServingAdapter;

import java.util.ArrayList;

@EActivity(R.layout.list_layout)
public class SpecificServingsActivity extends FragmentActivity {

    private ArrayList<DumplingServings> dumplingServings;
    private DumplingServingAdapter dumplingServingAdapter;

    @ViewById(android.R.id.list)
    protected ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DumplingDefaults dumplingDefaults = new DumplingDefaults(this);
        dumplingServings =
                (ArrayList<DumplingServings>)getIntent().getSerializableExtra(DumplingServings.class.getName());

        dumplingServingAdapter = new DumplingServingAdapter(this, dumplingDefaults, dumplingServings);
        initActionBar();
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
    }

    @AfterViews
    protected void populateListView() {

        View settingView = getLayoutInflater().inflate(R.layout.list_footer, null);
        listView.addFooterView(settingView);
        listView.setAdapter(dumplingServingAdapter);

        DismissArrayAdapterItemListViewTouchListener touchListener
                = new DismissArrayAdapterItemListViewTouchListener(listView, dumplingServingAdapter);

        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }

    public void addDumpling(View button) {
        dumplingServings.add(new DumplingServings(new Dumpling(""), 0));
        dumplingServingAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DumplingServings.class.getName(), dumplingServings);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
