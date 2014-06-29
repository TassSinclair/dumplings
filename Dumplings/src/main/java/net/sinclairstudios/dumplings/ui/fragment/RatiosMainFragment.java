package net.sinclairstudios.dumplings.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.*;
import com.googlecode.androidannotations.annotations.*;
import net.sinclairstudios.dumplings.DumplingsRatingsDataController;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.ui.activity.RatingsActivity_;
import net.sinclairstudios.dumplings.ui.activity.YourOrderActivity_;
import net.sinclairstudios.dumplings.domain.DumplingOrderListFactory;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@EFragment(R.layout.ratios_main_layout)
public class RatiosMainFragment extends Fragment {

    private final static int MODIFYING_RATINGS = 1;

    private final DumplingOrderListFactory orderListFactory = new DumplingOrderListFactory();

    @ViewById
    protected SeekBar howManyPeopleSeekBar;

    @ViewById
    protected Switch preferEvenServingsSwitch;

    @ViewById
    protected Button specificServingsButton;

    @ViewById
    protected TextView howManyPeopleTextView;

    private DumplingsRatingsDataController dumplingsRatingsDataController =
            new DumplingsRatingsDataController();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences();
        if (preferences.contains(DumplingRating.class.getName())) {
            dumplingsRatingsDataController.populate(preferences);
        } else {
            dumplingsRatingsDataController.populate(getResources());
        }
    }

    @Click
    protected void ratingsAndRatiosButton() {
        Intent intent = new Intent(getActivity(), RatingsActivity_.class);
        intent.putExtra(DumplingRating.class.getName(), dumplingsRatingsDataController.get());
        startActivityForResult(intent, MODIFYING_RATINGS);
    }


    @Click
    protected void calculateRatiosButton() {
        Intent intent = new Intent(getActivity(), YourOrderActivity_.class);
        int requiredServings = howManyPeopleSeekBar.getProgress() + 1;
        ArrayList<DumplingServings> dumplingServings = orderListFactory.createFromDumplingRatings(
                dumplingsRatingsDataController.get(), requiredServings, preferMultiplesOf());
        intent.putExtra(DumplingServings.class.getName(), dumplingServings);
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
        Log.w(getActivity().getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getActivity().getLocalClassName(), "uncatered request code " + requestCode);
    }

    private void onModifyingRatingsResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            dumplingsRatingsDataController.set((ArrayList<DumplingRating>)
                    data.getSerializableExtra(DumplingRating.class.getName()));
        } else {
            logUncateredResultCode("modifying ratings", resultCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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

        editor
            .putInt(SeekBar.class.getName(), howManyPeopleSeekBar.getProgress())
            .putBoolean(Switch.class.getName(), preferEvenServingsSwitch.isChecked())
            .commit();
    }

    private SharedPreferences getPreferences() {
        return getActivity().getPreferences(Activity.MODE_PRIVATE);
    }
}
