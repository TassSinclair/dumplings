package net.sinclairstudios.android.dumplings.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.*;

import net.sinclairstudios.android.dumplings.domain.DumplingOrderList;
import net.sinclairstudios.android.dumplings.DumplingsRatingListDataController;
import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingOrder;
import net.sinclairstudios.android.dumplings.domain.DumplingRating;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.util.TextViewUpdater;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.how_many_people_layout)
public class HowManyPeopleActivity extends Activity {

    private final static int MODIFYING_CHOICES_AND_RATIOS = 1;

    @ViewById
    protected SeekBar howManyPeopleSeekBar;

    @ViewById
    protected TextView howManyPeopleTextView;

    private DumplingsRatingListDataController dumplingsRatingListDataController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dumplingsRatingListDataController = new DumplingsRatingListDataController();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (preferences.contains(DumplingRatingList.class.getName())) {
            dumplingsRatingListDataController.populate(preferences);
        } else {
            dumplingsRatingListDataController.populate(getResources());
        }
    }

    @Click
    public void calculateRatiosButton() {
        Intent intent = new Intent(this, YourOrderActivity_.class);
        DumplingRatingList dumplingRatings = dumplingsRatingListDataController.get();
        intent.putExtra(DumplingOrderList.class.getName(), new DumplingOrderList(dumplingRatings,
                (Integer) howManyPeopleSeekBar.getProgress()));
        startActivity(intent);
    }

    @AfterViews
    protected void initHowManyPeopleSpinner() {
        howManyPeopleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private final TextViewUpdater textViewUpdater =
                    new TextViewUpdater(howManyPeopleTextView, getString(R.string.howManyServingsPrompt));

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                textViewUpdater.update(value + 1);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        howManyPeopleSeekBar.setProgress(preferences.getInt(SeekBar.class.getName(), 0));
        howManyPeopleSeekBar.refreshDrawableState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.how_many_people_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customiseMenuItem:
                navigateToChoicesAndRatiosActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logUncateredResultCode(String request, int resultCode) {
        Log.w(getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getLocalClassName(), "uncatered request code " + requestCode);
    }

    private void onModifyingChoicesAndRatiosResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            dumplingsRatingListDataController.set((DumplingRatingList)
                    data.getSerializableExtra(DumplingRatingList.class.getName()));
        } else {
            logUncateredResultCode("modifying choices and ratios", resultCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MODIFYING_CHOICES_AND_RATIOS:
                onModifyingChoicesAndRatiosResult(resultCode, data);
                break;
            default:
                logUncateredRequestCode(requestCode);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        final SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        dumplingsRatingListDataController.depopulate(editor);

        editor.putInt(SeekBar.class.getName(), howManyPeopleSeekBar.getProgress());
        editor.commit();
    }

    private void navigateToChoicesAndRatiosActivity() {
        Intent intent = new Intent(this, ChoicesAndRatiosActivity_.class);
        intent.putExtra(DumplingRatingList.class.getName(), dumplingsRatingListDataController.get());
        startActivityForResult(intent, MODIFYING_CHOICES_AND_RATIOS);
    }
}
