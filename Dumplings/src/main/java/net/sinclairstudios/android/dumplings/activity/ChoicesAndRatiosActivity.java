package net.sinclairstudios.android.dumplings.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingViewHook;
import net.sinclairstudios.android.dumplings.layout.LineSeparatorLinearLayout;

@EActivity(R.layout.choices_and_ratios_layout)
public class ChoicesAndRatiosActivity extends Activity {

    private DumplingRatingList dumplingRatingList;

    @ViewById
    protected LineSeparatorLinearLayout choicesAndRatiosRowHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dumplingRatingList = (DumplingRatingList) getIntent().getSerializableExtra(DumplingRatingList.class.getName());
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
        choicesAndRatiosRowHolder.removeAllViews();

        for (DumplingRatingViewHook dumplingRatingViewHook : dumplingRatingList.getDumplingRatingViewHooks()) {
            ViewGroup row = (ViewGroup) getLayoutInflater().inflate(R.layout.choices_and_ratios_row, null);
            EditText dumplingNameEditText = (EditText) row.findViewById(R.id.dumplingNameEditText);

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
        intent.putExtra(DumplingRatingList.class.getName(), dumplingRatingList);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
