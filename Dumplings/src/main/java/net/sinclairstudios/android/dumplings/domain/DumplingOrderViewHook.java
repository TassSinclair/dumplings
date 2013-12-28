package net.sinclairstudios.android.dumplings.domain;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.sinclairstudios.android.dumplings.R;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;

public class DumplingOrderViewHook {
    private final DumplingOrder dumplingOrder;

    public DumplingOrderViewHook(DumplingOrder dumplingOrder) {
        this.dumplingOrder = dumplingOrder;
    }

    public void bind(TextView nameTextView, TableLayout checkboxTableLayout, CountTracker masterCountTracker,
                     Context context) {
        CountTracker thisCountTracker = new CountTracker(dumplingOrder.getServings());
        thisCountTracker.addOnAddListener(new TextViewUpdatingCountTrackerListener(nameTextView,
                "{} " + dumplingOrder.getDumpling().getName()));
        thisCountTracker.add(0);
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                createListener(masterCountTracker, thisCountTracker);

        ViewGroup row = null;
        for (int i = 0; i < dumplingOrder.getServings(); ++i) {
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
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(DumplingOrderViewHook.class.getName(),
                        "Change arrival event received: " + dumplingOrder + ", " + b);
                thisCountTracker.add(b ? -1 : 1);
                masterCountTracker.add(b ? -1 : 1);
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
}
