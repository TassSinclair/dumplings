package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingRatingViewHook;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DumplingRatingAdapter extends ArrayAdapter<DumplingRating> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;
    private int rowWithFocus = -1;
    private int indexWithFocus = -1;

    public DumplingRatingAdapter(Context context, List<DumplingRating> dumplingRatings) {
        super(context, 0, dumplingRatings);
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    @Nullable
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ratings_row, parent, false);
        }

        final ListenerTrackingEditText dumplingNameEditText =
                (ListenerTrackingEditText) row.findViewById(R.id.dumplingNameEditText);

        dumplingNameEditText.clearTextChangedListeners();

        dumplingNameEditText.setAdapter(autocompleteAdapterFactory.createAdapter());
        dumplingNameEditText.addTextChangedListener(autocompleteAdapterFactory
                .createListener((ImageView) row.findViewById(R.id.dumplingImage)));
        RatingBar dumplingRatingBar = (RatingBar) row.findViewById(R.id.dumplingRatioRatingBar);

        new DumplingRatingViewHook(getItem(position)).bind(dumplingNameEditText, dumplingRatingBar);

        dumplingNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexWithFocus = dumplingNameEditText.getSelectionStart();
            }
        });

        dumplingNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rowWithFocus = position;
                    indexWithFocus = dumplingNameEditText.getSelectionStart();
                }
                Log.w(DumplingServingAdapter.class.getName(), "focus changed for " + position + ": " + hasFocus);
            }
        });
        dumplingNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rowWithFocus = -1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (position == rowWithFocus) {
            dumplingNameEditText.setSelection(indexWithFocus);
            dumplingNameEditText.requestFocus();
        }

        dumplingNameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rowWithFocus = -1;
            }
        });

        return row;
    }
}

