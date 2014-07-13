package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingOrder;
import net.sinclairstudios.dumplings.domain.DumplingOrderViewHook;
import net.sinclairstudios.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.util.CountTracker;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingOrderAdapter extends ArrayAdapter<DumplingOrder> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;
    private final CountTracker masterCountTracker;

    public DumplingOrderAdapter(Context context, List<DumplingOrder> dumplingOrders,
                                CountTracker masterCountTracker) {
        super(context, 0, dumplingOrders);
        this.masterCountTracker = masterCountTracker;
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DumplingOrder dumplingOrder = getItem(position);

        View row = convertView;

//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.your_order_row, parent, false);
//        }

        TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingNameTextView);
        ImageView dumplingImageView = (ImageView) row.findViewById(R.id.dumplingImage);

        DumplingOrderViewHook viewHook = new DumplingOrderViewHook(dumplingOrder);

        autocompleteAdapterFactory.updateDumplingImageFromLabel(
                dumplingOrder.getServings().getDumpling().getName(), dumplingImageView);
        viewHook.bind(dumplingNameTextView,
                (TableLayout) row.findViewById(R.id.dumplingCheckboxHolder),
                masterCountTracker, getContext());

        return row;
    }
}
