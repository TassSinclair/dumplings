package net.sinclairstudios.android.dumplings.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingServings;
import net.sinclairstudios.android.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.android.dumplings.layout.LineSeparatorLinearLayout;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.your_order_layout)
public class YourOrderActivity extends Activity {

    private ArrayList<DumplingServings> dumplingServings;

    @ViewById
    protected LineSeparatorLinearLayout yourOrderRowHolder;
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

        List<DumplingServingsViewHook> viewHooks = DumplingServingsViewHook.createFrom(dumplingServings);

        for (DumplingServingsViewHook dumplingServingsViewHook : viewHooks) {
            View repeatableDumplingRowLayout = getLayoutInflater().inflate(R.layout.your_order_row, null);
            yourOrderRowHolder.addView(repeatableDumplingRowLayout);
            dumplingServingsViewHook.bind((TextView) repeatableDumplingRowLayout.findViewById(R.id.dumplingNameTextView),
                    (TableLayout) repeatableDumplingRowLayout.findViewById(R.id.dumplingCheckboxHolder),
                    masterCountTracker, this);
        }
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
