package net.sinclairstudios.android.dumplings.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.*;

import net.sinclairstudios.android.dumplings.DumplingsRatingListDataController;
import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingOrderList;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@EFragment(R.layout.how_many_people_layout)
public class HowManyPeopleFragment extends Fragment {

    private final static int MODIFYING_CHOICES_AND_RATIOS = 1;

    @ViewById
    protected SeekBar howManyPeopleSeekBar;

    @ViewById
    protected TextView howManyPeopleTextView;

    private DumplingsRatingListDataController dumplingsRatingListDataController =
            new DumplingsRatingListDataController();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences();
        if (preferences.contains(DumplingRatingList.class.getName())) {
            dumplingsRatingListDataController.populate(preferences);
        } else {
            dumplingsRatingListDataController.populate(getResources());
        }
    }

    @Click
    protected void choicesAndRatiosButton() {
        Intent intent = new Intent(getActivity(), ChoicesAndRatiosActivity_.class);
        intent.putExtra(DumplingRatingList.class.getName(), dumplingsRatingListDataController.get());
        startActivityForResult(intent, MODIFYING_CHOICES_AND_RATIOS);
    }


    @Click
    protected void calculateRatiosButton() {
        Intent intent = new Intent(getActivity(), YourOrderActivity_.class);
        DumplingRatingList dumplingRatings = dumplingsRatingListDataController.get();
        intent.putExtra(DumplingOrderList.class.getName(),
                new DumplingOrderList(dumplingRatings, howManyPeopleSeekBar.getProgress() + 1));
        startActivity(intent);
    }

    @AfterViews
    protected void initHowManyPeopleSpinner() {
        SeekBar.OnSeekBarChangeListener howManyPeopleChangeListener = createHowManyPeopleChangeListener();
        int progress = getPreferences().getInt(SeekBar.class.getName(), 0);

        howManyPeopleChangeListener.onProgressChanged(howManyPeopleSeekBar, progress, false);
        // Call it once to fake an update, if the update is no change.
        howManyPeopleSeekBar.setOnSeekBarChangeListener(howManyPeopleChangeListener);
        howManyPeopleSeekBar.setProgress(progress);

    }

    private SeekBar.OnSeekBarChangeListener createHowManyPeopleChangeListener() {
        return new SeekBar.OnSeekBarChangeListener() {

            private final TextViewUpdater textViewUpdater =
                    new TextViewUpdater(howManyPeopleTextView, getString(R.string.howManyServings));

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                textViewUpdater.update(value + 1);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }



    private void logUncateredResultCode(String request, int resultCode) {
        Log.w(getActivity().getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getActivity().getLocalClassName(), "uncatered request code " + requestCode);
    }

    private void onModifyingChoicesAndRatiosResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            dumplingsRatingListDataController.set((DumplingRatingList)
                    data.getSerializableExtra(DumplingRatingList.class.getName()));
        } else {
            logUncateredResultCode("modifying choices and ratios", resultCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getPreferences().edit();
        dumplingsRatingListDataController.depopulate(editor);

        editor.putInt(SeekBar.class.getName(), howManyPeopleSeekBar.getProgress());
        editor.commit();
    }

    private SharedPreferences getPreferences() {
        return getActivity().getPreferences(Activity.MODE_PRIVATE);
    }
}
