package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingOrder;
import net.sinclairstudios.dumplings.domain.DumplingOrderViewHook;
import net.sinclairstudios.util.CountTracker;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingOrderAdapter extends ArrayAdapter<DumplingOrder> {

    private final DumplingDefaults dumplingDefaults;
    private final CountTracker masterCountTracker;

    public DumplingOrderAdapter(Context context,
                                DumplingDefaults dumplingDefaults,
                                List<DumplingOrder> dumplingOrders,
                                CountTracker masterCountTracker) {
        super(context, 0, dumplingOrders);
        this.masterCountTracker = masterCountTracker;
        this.dumplingDefaults = dumplingDefaults;
    }

    private View createOrRecycleView(ViewGroup parent, View toBeRecycled) {
        if (toBeRecycled == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.your_order_row, parent, false);
        }
        return toBeRecycled;
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DumplingOrder dumplingOrder = getItem(position);
        final View row = createOrRecycleView(parent, convertView);

        TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingNameTextView);
        ImageView dumplingImageView = (ImageView) row.findViewById(R.id.dumplingImage);

        DumplingOrderViewHook viewHook = new DumplingOrderViewHook(dumplingOrder);

        Drawable icon = dumplingDefaults.getIcon(dumplingOrder.getServings().getDumpling().getName());
        dumplingImageView.setImageDrawable(icon);
        viewHook.bind(dumplingNameTextView,
                (TableLayout) row.findViewById(R.id.dumplingCheckboxHolder),
                masterCountTracker, getContext());

        return row;
    }
}
