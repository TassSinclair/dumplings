package net.sinclairstudios.dumplings.ui.widgets;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.ui.binding.DumplingBinderFactory;
import net.sinclairstudios.dumplings.ui.fragment.DumplingNameDialogFragment;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumplingServingAdapter extends ArrayAdapter<DumplingServings> {

    private final DumplingBinderFactory dumplingBinderFactory;
    private final FragmentManager fragmentManager;
    private final DumplingDefaults dumplingDefaults;
    private final Map<View, TextViewUpdater> textViewUpdaters = new HashMap<View, TextViewUpdater>();

    public DumplingServingAdapter(FragmentActivity fragmentActivity,
                                 DumplingDefaults dumplingDefaults,
                                 List<DumplingServings> dumplingServings) {
        super(fragmentActivity, 0, dumplingServings);
        this.dumplingDefaults = dumplingDefaults;
        dumplingBinderFactory = new DumplingBinderFactory(fragmentActivity, dumplingDefaults);
        fragmentManager = fragmentActivity.getFragmentManager();
    }

    private View createOrRecycleView(ViewGroup parent, View toBeRecycled) {
        if (toBeRecycled == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.specific_servings_row, parent, false);
        }
        return toBeRecycled;
    }

    @Nullable
    @Override
    public View getView(final int position, View convertView, @NotNull ViewGroup parent) {

        final DumplingServings dumplingServings = getItem(position);
        final View row = createOrRecycleView(parent, convertView);
        final TextView dumplingServingCountTextView = (TextView) row.findViewById(R.id.dumplingServingCountTextView);
        final TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingNameTextView);
        final ImageButton dumplingNameButton = (ImageButton) row.findViewById(R.id.dumplingNameButton);
        final SeekBar seekBar = (SeekBar) row.findViewById(R.id.dumplingServingSeekBar);

        dumplingNameTextView.setText(dumplingServings.getDumpling().getName());
        ((ImageView) row.findViewById(R.id.dumplingImage)).setImageDrawable(
                dumplingDefaults.getIcon(dumplingServings.getDumpling().getName()));

        TextViewUpdater dumplingServingTextUpdater = textViewUpdaters.get(row);
        if (dumplingServingTextUpdater == null) {
            dumplingServingTextUpdater = new TextViewUpdater(dumplingServingCountTextView);
            textViewUpdaters.put(row, dumplingServingTextUpdater);
        }

        dumplingBinderFactory.bindSeekBar(dumplingServings, dumplingServingTextUpdater, seekBar);

        dumplingNameButton.setOnClickListener(
                new DumplingNameDialogFragment.Spawner(fragmentManager, dumplingBinderFactory, dumplingServings));

        return row;
    }
}
