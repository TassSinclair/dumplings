package net.sinclairstudios.dumplings.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.*;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.dumplings.DumplingsRatingsDataController;
import net.sinclairstudios.dumplings.DumplingsServingsDataController;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.calculation.DumplingServingAccumulator;
import net.sinclairstudios.dumplings.domain.DumplingOrderListFactory;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.main_layout)
public class MainActivity extends FragmentActivity {

    private final static int MODIFYING_RATINGS = 1;
    private final static int MODIFYING_SPECIFIC_SERVINGS = 2;

    private final DumplingOrderListFactory orderListFactory = new DumplingOrderListFactory();

    @ViewById
    protected SeekBar howManyPeopleSeekBar;

    @ViewById
    protected Switch preferEvenServingsSwitch;

    @ViewById
    protected CheckBox specificServingsCheckbox;

    @ViewById
    protected Button specificServingsButton;

    @ViewById
    protected CheckBox ratingsAndRatiosCheckbox;

    @ViewById
    protected Button ratingsAndRatiosButton;

    @ViewById
    protected TextView howManyPeopleTextView;

    @ViewById
    protected ImageView specificServingsGuide;

    @ViewById
    protected ImageView ratingsGuide;

    private DumplingsRatingsDataController dumplingsRatingsDataController =
            new DumplingsRatingsDataController();
    private DumplingsServingsDataController dumplingsServingsDataController =
            new DumplingsServingsDataController();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences();
        if (preferences.contains(DumplingRating.class.getName())) {
            dumplingsRatingsDataController.populate(preferences);
        } else {
            dumplingsRatingsDataController.populate(getResources());
        }
        if (preferences.contains(DumplingServings.class.getName())) {
            dumplingsServingsDataController.populate(preferences);
        } else {
            dumplingsServingsDataController.populate(getResources());
        }
    }

    @Click
    protected void specificServingsButton() {
        Intent intent = new Intent(this, SpecificServingsActivity_.class);
        intent.putExtra(DumplingServings.class.getName(), dumplingsServingsDataController.get());
        startActivityForResult(intent, MODIFYING_SPECIFIC_SERVINGS);
    }

    @Click
    protected void ratingsAndRatiosButton() {
        Intent intent = new Intent(this, RatingsActivity_.class);
        intent.putExtra(DumplingRating.class.getName(), dumplingsRatingsDataController.get());
        startActivityForResult(intent, MODIFYING_RATINGS);
    }


    @Click
    protected void calculateRatiosButton() {
        Intent intent = new Intent(this, YourOrderActivity_.class);
        int remainingServings = howManyPeopleSeekBar.getProgress() + 1;
        DumplingServingAccumulator accumulator = new DumplingServingAccumulator();
        if (specificServingsCheckbox.isChecked()) {
            List<DumplingServings> limitedServings =
                    orderListFactory.limitDumplingOrders(dumplingsServingsDataController.get(), remainingServings);
            accumulator.add(limitedServings);
            remainingServings -= countAllDumplingServings(limitedServings);
        }

        if (remainingServings > 0) {
            accumulator.add(orderListFactory.createFromDumplingRatings(
                    dumplingsRatingsDataController.get(), remainingServings, preferMultiplesOf()));
        }
        intent.putExtra(DumplingServings.class.getName(), accumulator.getAll());
        startActivity(intent);
    }

    private int preferMultiplesOf() {
        return preferEvenServingsSwitch.isChecked()? 2 : 1;
    }

    @AfterViews
    protected void initControls() {
        // Seek bar
        SeekBar.OnSeekBarChangeListener howManyPeopleChangeListener = createHowManyPeopleChangeListener();
        int progress = getPreferences().getInt(SeekBar.class.getName(), 0);
        howManyPeopleChangeListener.onProgressChanged(howManyPeopleSeekBar, progress, false);
        // Call it once to fake an update, if the update is no change.
        howManyPeopleSeekBar.setOnSeekBarChangeListener(howManyPeopleChangeListener);
        howManyPeopleSeekBar.setProgress(progress);

        // Switch
        boolean preferMultiplesOfTwo = getPreferences().getBoolean(Switch.class.getName(), false);
        preferEvenServingsSwitch.setChecked(preferMultiplesOfTwo);

        // Checkboxes
        boolean useSpecificServings = getPreferences().getBoolean(CheckBox.class.getName(), false);
        specificServingsCheckbox.setChecked(useSpecificServings);
        CheckBox.OnCheckedChangeListener onCheckedChangeListener =
                createEnableButtonWhenCheckedListener(specificServingsButton);
        specificServingsCheckbox.setOnCheckedChangeListener(
                onCheckedChangeListener);
        onCheckedChangeListener.onCheckedChanged(specificServingsCheckbox, specificServingsCheckbox.isChecked());

        onCheckedChangeListener = createEnableButtonWhenCheckedListener(ratingsAndRatiosButton);
        ratingsAndRatiosCheckbox.setOnCheckedChangeListener(onCheckedChangeListener);
        onCheckedChangeListener.onCheckedChanged(ratingsAndRatiosCheckbox, ratingsAndRatiosCheckbox.isChecked());
    }

    private int countAllDumplingServings(List<DumplingServings> dumplingServingsList) {
        int totalServingsWanted = 0;

        for (DumplingServings servings : dumplingServingsList) {
            totalServingsWanted += servings.getServings();
        }
        return totalServingsWanted;
    }

    @AfterViews
    protected void initHowManyPeopleGuides() {
        final int maxWeight = getResources().getInteger(R.integer.maxServings) + 1;
        if (specificServingsCheckbox.isChecked()) {
            final int totalServingsWanted =
                    Math.min(countAllDumplingServings(dumplingsServingsDataController.get()), maxWeight);
            // Weights, so we inverse.
            setGuideWeight(ratingsGuide, totalServingsWanted);
            setGuideWeight(specificServingsGuide, maxWeight - totalServingsWanted);
        } else {
            setGuideWeight(ratingsGuide, 0);
            setGuideWeight(specificServingsGuide, maxWeight);
        }
    }

    private void setGuideWeight(ImageView guide, int weight) {
        guide.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, weight));
    }

    private CompoundButton.OnCheckedChangeListener
    createEnableButtonWhenCheckedListener(final Button specificServingsButton) {
        return new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                specificServingsButton.setEnabled(isChecked);
                initHowManyPeopleGuides();
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener createHowManyPeopleChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {

            private final TextViewUpdater textViewUpdater =
                    new TextViewUpdater(howManyPeopleTextView, getString(R.string.howManyServings));

            @Override
            public void onProgressChanged(@NotNull SeekBar seekBar, int value, boolean b) {
                textViewUpdater.update(value + 1);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(@NotNull SeekBar seekBar) {}
        };
    }



    private void logUncateredResultCode(String request, int resultCode) {
        Log.w(getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getLocalClassName(), "uncatered request code " + requestCode);
    }

    private void onModifyingRatingsResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            dumplingsRatingsDataController.set((ArrayList<DumplingRating>)
                    data.getSerializableExtra(DumplingRating.class.getName()));
            initHowManyPeopleGuides();
        } else {
            logUncateredResultCode("modifying ratings", resultCode);
        }
    }

    private void onModifyingSpecificServingsResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            dumplingsServingsDataController.set((ArrayList<DumplingServings>)
                    data.getSerializableExtra(DumplingServings.class.getName()));
            initHowManyPeopleGuides();
        } else {
            logUncateredResultCode("modifying specific servings", resultCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MODIFYING_SPECIFIC_SERVINGS:
                onModifyingSpecificServingsResult(resultCode, data);
                break;
            case MODIFYING_RATINGS:
                onModifyingRatingsResult(resultCode, data);
                break;
            default:
                logUncateredRequestCode(requestCode);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getPreferences().edit();
        dumplingsRatingsDataController.depopulate(editor);
        dumplingsServingsDataController.depopulate(editor);

        editor.putInt(SeekBar.class.getName(), howManyPeopleSeekBar.getProgress());
        editor.putBoolean(Switch.class.getName(), preferEvenServingsSwitch.isChecked());
        editor.putBoolean(CheckBox.class.getName(), specificServingsCheckbox.isChecked());
        editor.commit();
    }

    private SharedPreferences getPreferences() {
        return getPreferences(Activity.MODE_PRIVATE);
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
