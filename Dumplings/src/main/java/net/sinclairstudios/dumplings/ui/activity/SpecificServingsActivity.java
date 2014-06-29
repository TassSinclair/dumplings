package net.sinclairstudios.dumplings.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.dumplings.ui.layout.LineSeparatorLinearLayout;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.specific_servings_layout)
public class SpecificServingsActivity extends Activity {

    private ArrayList<DumplingServings> dumplingServings;

    @ViewById
    protected LineSeparatorLinearLayout specificServingsRowHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dumplingServings =
                (ArrayList<DumplingServings>)getIntent().getSerializableExtra(DumplingServings.class.getName());
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
    protected void hydrateRows() {
        DumplingNameAutocompleteAdapterFactory adapterFactory = new DumplingNameAutocompleteAdapterFactory(this);
        specificServingsRowHolder.removeAllViews();

        List<DumplingServingsViewHook> viewHooks = DumplingServingsViewHook.createFrom(dumplingServings);
        for (DumplingServingsViewHook viewHook : viewHooks) {
            ViewGroup row = (ViewGroup) getLayoutInflater().inflate(R.layout.specific_servings_row, null);
            AutoCompleteTextView dumplingNameEditText =
                    (AutoCompleteTextView) row.findViewById(R.id.dumplingNameEditText);
            dumplingNameEditText.setAdapter(adapterFactory.createAdapter());
            dumplingNameEditText.addTextChangedListener(adapterFactory
                    .createListener((ImageView) row.findViewById(R.id.dumplingImage)));
            TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingServingCountTextView);

            dumplingNameEditText.setId(specificServingsRowHolder.findUnusedId());
            SeekBar seekBar = (SeekBar) row.findViewById(R.id.dumplingServingSeekBar);
            seekBar.setId(specificServingsRowHolder.findUnusedId());
            viewHook.bind(dumplingNameEditText);
            viewHook.bind(seekBar, dumplingNameTextView);

            specificServingsRowHolder.addView(row);
        }
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
