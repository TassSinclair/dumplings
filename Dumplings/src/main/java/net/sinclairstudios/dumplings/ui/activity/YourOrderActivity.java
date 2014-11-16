package net.sinclairstudios.dumplings.ui.activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingOrder;
import net.sinclairstudios.dumplings.ui.widgets.DumplingOrderAdapter;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;

import java.util.ArrayList;

@EActivity(R.layout.your_order_layout)
public class YourOrderActivity extends ListActivity {

    private ArrayList<DumplingOrder> dumplingOrders;

    @ViewById
    protected TextView servingCountdownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dumplingOrders =
                (ArrayList<DumplingOrder>) getIntent().getSerializableExtra(DumplingOrder.class.getName());
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @AfterViews
    protected void initRows() {
        int totalDumplings = 0;
        for (DumplingOrder dumplingOrder : dumplingOrders) {
            totalDumplings += dumplingOrder.getServings().getServings();
        }
        CountTracker masterCountTracker = new CountTracker(totalDumplings);
        masterCountTracker.addOnAddListener(new TextViewUpdatingCountTrackerListener(servingCountdownTextView));
        DumplingDefaults dumplingDefaults = new DumplingDefaults(this);
        getListView().setAdapter(new DumplingOrderAdapter(this, dumplingDefaults, dumplingOrders, masterCountTracker));
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
