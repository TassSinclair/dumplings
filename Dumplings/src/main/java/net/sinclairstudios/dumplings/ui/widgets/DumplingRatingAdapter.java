package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingRatingViewHook;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingRatingAdapter extends ArrayAdapter<DumplingRating> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;

    public DumplingRatingAdapter(Context context, List<DumplingRating> dumplingRatings) {
        super(context, 0, dumplingRatings);
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ratings_row, parent, false);
//        }

        AutoCompleteTextView dumplingNameEditText =
                (AutoCompleteTextView) row.findViewById(R.id.dumplingNameEditText);
        dumplingNameEditText.setAdapter(autocompleteAdapterFactory.createAdapter());
        dumplingNameEditText.addTextChangedListener(autocompleteAdapterFactory
                .createListener((ImageView) row.findViewById(R.id.dumplingImage)));
        RatingBar dumplingRatingBar = (RatingBar) row.findViewById(R.id.dumplingRatioRatingBar);

        new DumplingRatingViewHook(getItem(position)).bind(dumplingNameEditText, dumplingRatingBar);

        return row;
    }
}
