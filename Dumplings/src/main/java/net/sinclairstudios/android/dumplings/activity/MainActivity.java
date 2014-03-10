package net.sinclairstudios.android.dumplings.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.android.dumplings.DumplingsRatingListDataController;
import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.android.dumplings.domain.DumplingOrderList;
import net.sinclairstudios.android.dumplings.domain.DumplingOrderListFactory;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.util.TextViewUpdater;

@EActivity(R.layout.main_layout)
public class MainActivity extends FragmentActivity {

    private final static int MODIFYING_CHOICES_AND_RATIOS = 1;

    private final DumplingOrderListFactory orderListFactory = new DumplingOrderListFactory();

    @ViewById
    protected SeekBar howManyPeopleSeekBar;

    @ViewById
    protected Switch preferEvenServingsSwitch;

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
        Intent intent = new Intent(this, ChoicesAndRatiosActivity_.class);
        intent.putExtra(DumplingRatingList.class.getName(), dumplingsRatingListDataController.get());
        startActivityForResult(intent, MODIFYING_CHOICES_AND_RATIOS);
    }


    @Click
    protected void calculateRatiosButton() {
        Intent intent = new Intent(this, YourOrderActivity_.class);
        DumplingRatingList dumplingRatings = dumplingsRatingListDataController.get();
        intent.putExtra(DumplingOrderList.class.getName(),
                orderListFactory.createFromDumplingRatings(dumplingRatings.get(),
                        howManyPeopleSeekBar.getProgress() + 1, preferMultiplesOf()));
        startActivity(intent);
    }

    private int preferMultiplesOf() {
        return preferEvenServingsSwitch.isChecked()? 2 : 1;
    }

    @AfterViews
    protected void initHowManyPeopleSpinner() {
        SeekBar.OnSeekBarChangeListener howManyPeopleChangeListener = createHowManyPeopleChangeListener();
        int progress = getPreferences().getInt(SeekBar.class.getName(), 0);
        boolean preferMultiplesOfTwo = getPreferences().getBoolean(Switch.class.getName(), false);
        preferEvenServingsSwitch.setChecked(preferMultiplesOfTwo);
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
        Log.w(getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getLocalClassName(), "uncatered request code " + requestCode);
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
        editor.putBoolean(Switch.class.getName(), preferEvenServingsSwitch.isChecked());
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
