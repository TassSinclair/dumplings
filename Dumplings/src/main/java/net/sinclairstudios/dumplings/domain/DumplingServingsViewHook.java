package net.sinclairstudios.dumplings.domain;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;

import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdater;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DumplingServingsViewHook {

    private final DumplingServings dumplingServings;

    public DumplingServingsViewHook(DumplingServings dumplingServings) {
        this.dumplingServings = dumplingServings;
    }

    public void bind(TextView nameTextView, TableLayout checkboxTableLayout, CountTracker masterCountTracker,
                     Context context) {
        CountTracker thisCountTracker = new CountTracker(dumplingServings.getServings());
        thisCountTracker.addOnAddListener(new TextViewUpdatingCountTrackerListener(nameTextView,
                "{} " + dumplingServings.getDumpling().getName()));
        thisCountTracker.add(0);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                createListener(masterCountTracker, thisCountTracker);

        ViewGroup row = null;
        for (int i = 0; i < dumplingServings.getServings(); ++i) {
            if (i % 5 == 0) {
                row = (TableRow) LayoutInflater.from(context).inflate(R.layout.your_order_servings_row, null);
                checkboxTableLayout.addView(row);
            }
            CheckBox checkbox = createCheckbox(context, onCheckedChangeListener);
            row.addView(checkbox);
        }
    }

    private CompoundButton.OnCheckedChangeListener createListener(final CountTracker masterCountTracker,
                                                                  final CountTracker thisCountTracker) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Log.d(DumplingServingsViewHook.class.getName(),
                        "Change arrival event received: " + dumplingServings + ", " + checked);
                thisCountTracker.add(checked ? -1 : 1);
                masterCountTracker.add(checked ? -1 : 1);
            }
        };
    }

    private CheckBox createCheckbox(Context context, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setButtonDrawable(R.drawable.dumpling_checkbox);
        checkBox.setBackgroundResource(R.drawable.dumpling_checkbox);
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        return checkBox;
    }

    public void bind(EditText editText) {
        editText.setText(dumplingServings.getDumpling().getName());
        editText.addTextChangedListener(createTextWatcher());
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(@NotNull CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                dumplingServings.setDumpling(new Dumpling(editable.toString()));
                Log.d(DumplingServingsViewHook.class.getName(), "Change name event received: " + dumplingServings);
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener createOnSeekBarChangeListener(final TextView textView) {
        return new SeekBar.OnSeekBarChangeListener() {

            private final TextViewUpdater textViewUpdater = new TextViewUpdater(textView, "{}");

            @Override
            public void onProgressChanged(@NotNull SeekBar seekBar, int progress, boolean fromUser) {
                dumplingServings.setServings(progress);
                textViewUpdater.update(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(@NotNull SeekBar seekBar) { }
        };
    }

    public void bind(SeekBar seekBar, TextView textView) {
        seekBar.setOnSeekBarChangeListener(null);
        seekBar.setProgress(dumplingServings.getServings());
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = createOnSeekBarChangeListener(textView);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        onSeekBarChangeListener.onProgressChanged(seekBar, seekBar.getProgress(), false);
    }
}
