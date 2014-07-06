package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.util.CountTracker;
import net.sinclairstudios.util.TextViewUpdatingCountTrackerListener;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingOrderAdapter extends ArrayAdapter<DumplingServings> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;
    private final CountTracker masterCountTracker;

    public DumplingOrderAdapter(Context context, List<DumplingServings> dumplingServings,
                                CountTracker masterCountTracker) {
        super(context, 0, dumplingServings);
        this.masterCountTracker = masterCountTracker;
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.your_order_row, parent, false);
//        }

        TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingNameTextView);
        ImageView dumplingImageView = (ImageView) row.findViewById(R.id.dumplingImage);

        DumplingServingsViewHook viewHook = new DumplingServingsViewHook(getItem(position));

        autocompleteAdapterFactory.updateDumplingImageFromLabel(
                viewHook.getDumplingServings().getDumpling().getName(), dumplingImageView);
        viewHook.bind(dumplingNameTextView,
                (TableLayout) row.findViewById(R.id.dumplingCheckboxHolder),
                masterCountTracker, getContext());

        return row;
    }
}
