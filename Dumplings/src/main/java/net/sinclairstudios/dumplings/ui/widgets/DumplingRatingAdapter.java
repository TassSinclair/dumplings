package net.sinclairstudios.dumplings.ui.widgets;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.ui.binding.DumplingBinderFactory;
import net.sinclairstudios.dumplings.ui.fragment.DumplingNameDialogFragment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingRatingAdapter extends ArrayAdapter<DumplingRating> {

    private final DumplingBinderFactory dumplingBinderFactory;
    private final FragmentManager fragmentManager;
    private final DumplingDefaults dumplingDefaults;

    public DumplingRatingAdapter(FragmentActivity fragmentActivity,
                                 DumplingDefaults dumplingDefaults,
                                 List<DumplingRating> dumplingRatings) {
        super(fragmentActivity, 0, dumplingRatings);
        this.dumplingDefaults = dumplingDefaults;
        dumplingBinderFactory = new DumplingBinderFactory(fragmentActivity, dumplingDefaults);
        fragmentManager = fragmentActivity.getFragmentManager();
    }

    private View createOrRecycleView(ViewGroup parent, View toBeRecycled) {
        if (toBeRecycled == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.ratings_row, parent, false);
        }
        return toBeRecycled;
    }

    @Nullable
    @Override
    public View getView(final int position, View convertView, @NotNull ViewGroup parent) {

        final DumplingRating dumplingRating = getItem(position);
        final View row = createOrRecycleView(parent, convertView);
        final TextView dumplingNameTextView = (TextView) row.findViewById(R.id.dumplingNameTextView);
        final RatingBar dumplingRatingBar = (RatingBar) row.findViewById(R.id.dumplingRatioRatingBar);

        dumplingNameTextView.setText(dumplingRating.getDumpling().getName());
        ((ImageView) row.findViewById(R.id.dumplingImage)).setImageDrawable(
                dumplingDefaults.getIcon(dumplingRating.getDumpling().getName()));

        dumplingBinderFactory.bindRatingBar(dumplingRating, dumplingRatingBar);

        dumplingNameTextView.setOnFocusChangeListener(
                new DumplingNameDialogFragment.Spawner(fragmentManager, dumplingBinderFactory, dumplingRating));

        return row;
    }
}

