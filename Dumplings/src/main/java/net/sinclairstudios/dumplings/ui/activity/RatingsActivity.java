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
import android.widget.RatingBar;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingRatingViewHook;
import net.sinclairstudios.dumplings.ui.layout.LineSeparatorLinearLayout;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.ratings_layout)
public class RatingsActivity extends Activity {

    private ArrayList<DumplingRating> dumplingRatingList;

    @ViewById
    protected LineSeparatorLinearLayout choicesAndRatiosRowHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dumplingRatingList =
                (ArrayList<DumplingRating>)getIntent().getSerializableExtra(DumplingRating.class.getName());
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
        choicesAndRatiosRowHolder.removeAllViews();

        List<DumplingRatingViewHook> viewHooks = DumplingRatingViewHook.createFrom(dumplingRatingList);
        for (DumplingRatingViewHook dumplingRatingViewHook : viewHooks) {
            ViewGroup row = (ViewGroup) getLayoutInflater().inflate(R.layout.ratings_row, null);
            AutoCompleteTextView dumplingNameEditText =
                    (AutoCompleteTextView) row.findViewById(R.id.dumplingNameEditText);
            dumplingNameEditText.setAdapter(adapterFactory.createAdapter());
            dumplingNameEditText.addTextChangedListener(adapterFactory
                    .createListener((ImageView) row.findViewById(R.id.dumplingImage)));
            dumplingNameEditText.setId(choicesAndRatiosRowHolder.findUnusedId());
            RatingBar dumplingRatingBar = (RatingBar) row.findViewById(R.id.dumplingRatioRatingBar);
            dumplingRatingBar.setId(choicesAndRatiosRowHolder.findUnusedId());
            dumplingRatingViewHook.bind(dumplingNameEditText, dumplingRatingBar);

            choicesAndRatiosRowHolder.addView(row);
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
        intent.putExtra(DumplingRating.class.getName(), dumplingRatingList);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
