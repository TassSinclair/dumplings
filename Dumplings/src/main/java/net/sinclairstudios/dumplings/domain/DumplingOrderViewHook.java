package net.sinclairstudios.dumplings.domain;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;

public class DumplingOrderViewHook {

    private final DumplingOrder dumplingOrder;

    public DumplingOrderViewHook(DumplingOrder dumplingOrder) {
        this.dumplingOrder = dumplingOrder;
    }

    public void bind(TextView nameTextView, TableLayout checkboxTableLayout, CountTracker masterCountTracker,
                     Context context) {
        CountTracker thisCountTracker = new CountTracker(dumplingOrder.getServings().getServings());
        nameTextView.setText("{} " + dumplingOrder.getServings().getDumpling().getName());
        thisCountTracker.addOnAddListener(new TextViewUpdatingCountTrackerListener(nameTextView));
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
                createListener(dumplingOrder, masterCountTracker, thisCountTracker);

        ViewGroup row = null;
        checkboxTableLayout.removeAllViews();
        int arrived = dumplingOrder.getArrived();
        for (int i = 0; i < dumplingOrder.getServings().getServings(); ++i) {
            if (i % 5 == 0) {
                row = (TableRow) LayoutInflater.from(context).inflate(R.layout.your_order_servings_row,
                        checkboxTableLayout, false);
                checkboxTableLayout.addView(row);
            }
            CheckBox checkbox = createCheckbox(context, onCheckedChangeListener, arrived > 0);
            --arrived;
            row.addView(checkbox);
        }
    }

    private CompoundButton.OnCheckedChangeListener createListener(final DumplingOrder dumplingOrder,
                                                                  final CountTracker masterCountTracker,
                                                                  final CountTracker thisCountTracker) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                int progress = (checked? 1 : -1);
                Log.d(DumplingOrderViewHook.class.getName(),
                        "Change arrival event received: " + dumplingOrder + ", " + checked);
                dumplingOrder.setArrived(dumplingOrder.getArrived() + progress);
                thisCountTracker.add(- progress);
                masterCountTracker.add(- progress);
            }
        };
    }

    private CheckBox createCheckbox(Context context, CompoundButton.OnCheckedChangeListener onCheckedChangeListener,
                                    boolean initialValue) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setButtonDrawable(R.drawable.dumpling_checkbox);
        checkBox.setBackgroundResource(R.drawable.dumpling_checkbox);
        checkBox.setChecked(initialValue);
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        return checkBox;
    }
}
