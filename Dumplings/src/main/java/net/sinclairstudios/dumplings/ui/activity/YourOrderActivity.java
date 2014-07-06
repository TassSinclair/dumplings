package net.sinclairstudios.dumplings.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import net.sinclairstudios.dumplings.ui.widgets.DumplingOrderAdapter;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.your_order_layout)
public class YourOrderActivity extends ListActivity {

    private ArrayList<DumplingServings> dumplingServings;

    @ViewById
    protected TextView servingCountdownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dumplingServings =
                (ArrayList<DumplingServings>) getIntent().getSerializableExtra(DumplingServings.class.getName());
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    protected void initRows() {
        int totalDumplings = 0;
        for (DumplingServings dumplingOrder : dumplingServings) {
            totalDumplings += dumplingOrder.getServings();
        }
        CountTracker masterCountTracker = new CountTracker(totalDumplings);
        masterCountTracker.addOnAddListener(new TextViewUpdatingCountTrackerListener(servingCountdownTextView,
                getString(R.string.servingCountdown)));
        masterCountTracker.add(0);

        getListView().setAdapter(new DumplingOrderAdapter(this, dumplingServings, masterCountTracker));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity_.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
