package net.sinclairstudios.dumplings.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.*;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import net.sinclairstudios.dumplings.DumplingsServingsDataController;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.calculation.DumplingServingAccumulator;
import net.sinclairstudios.dumplings.domain.DumplingOrder;
import net.sinclairstudios.dumplings.domain.DumplingOrderListFactory;
import net.sinclairstudios.dumplings.ui.activity.SpecificServingsActivity_;
import net.sinclairstudios.dumplings.ui.activity.YourOrderActivity_;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.specific_servings_main_layout)
public class ServingsMainFragment extends Fragment {

    private final static int MODIFYING_SPECIFIC_SERVINGS = 2;

    private final DumplingOrderListFactory orderListFactory = new DumplingOrderListFactory();

    @ViewById
    protected Button specificServingsButton;

    @ViewById
    protected TextView howManyPeopleTextView;

    private DumplingsServingsDataController dumplingsServingsDataController =
            new DumplingsServingsDataController();

    private TextViewUpdater howManyPeopleTextViewUpdater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences();
        if (preferences.contains(DumplingServings.class.getName())) {
            dumplingsServingsDataController.populate(preferences);
        } else {
            dumplingsServingsDataController.populate(getResources());
        }
    }

    @AfterViews
    protected void initHowManyPeopleTextViewUpdater() {
        howManyPeopleTextViewUpdater = new TextViewUpdater(howManyPeopleTextView,
                        getString(R.string.howManyServings));
        updateHowManyPeopleTextViewUpdater();
    }

    private void updateHowManyPeopleTextViewUpdater() {
        int totalServings = 0;
        for (DumplingServings serving : dumplingsServingsDataController.get()) {
            totalServings += serving.getServings();
        }
        howManyPeopleTextViewUpdater.update(totalServings);
    }

    @Click
    protected void specificServingsButton() {
        Intent intent = new Intent(getActivity(), SpecificServingsActivity_.class);
        intent.putExtra(DumplingServings.class.getName(), dumplingsServingsDataController.get());
        startActivityForResult(intent, MODIFYING_SPECIFIC_SERVINGS);
    }


    @Click
    protected void calculateRatiosButton() {
        Intent intent = new Intent(getActivity(), YourOrderActivity_.class);

        DumplingServingAccumulator accumulator = new DumplingServingAccumulator();
        accumulator.add(dumplingsServingsDataController.get());

        intent.putExtra(DumplingOrder.class.getName(), accumulator.getAll());
        startActivity(intent);
    }

    private void logUncateredResultCode(String request, int resultCode) {
        Log.w(getActivity().getLocalClassName(), "uncatered result code " + resultCode + " for " + request);
    }

    private void logUncateredRequestCode(int requestCode) {
        Log.w(getActivity().getLocalClassName(), "uncatered request code " + requestCode);
    }

    private void onModifyingSpecificServingsResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<DumplingServings> servings = (ArrayList<DumplingServings>)
                    data.getSerializableExtra(DumplingServings.class.getName());
            dumplingsServingsDataController.set(servings);
            updateHowManyPeopleTextViewUpdater();
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
        dumplingsServingsDataController.depopulate(editor);

        editor.commit();
    }

    private SharedPreferences getPreferences() {
        return getActivity().getPreferences(Activity.MODE_PRIVATE);
    }
}
