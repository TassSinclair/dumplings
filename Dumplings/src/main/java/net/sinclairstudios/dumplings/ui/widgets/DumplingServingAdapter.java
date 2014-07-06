package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingRatingViewHook;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.domain.DumplingServingsViewHook;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumplingServingAdapter extends ArrayAdapter<DumplingServings> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;
    private int rowWithFocus = -1;
    private int indexWithFocus = -1;
//    private final Map<DumplingServings, View> cache = new HashMap<DumplingServings, View>();

    public DumplingServingAdapter(Context context, List<DumplingServings> dumplingServings) {
        super(context, 0, dumplingServings);
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    @Nullable
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        DumplingServings dumplingServings = getItem(position);
//        if (cache.containsKey(dumplingServings)) {
//            return cache.get(dumplingServings);
//        }

        View row = convertView;

//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.specific_servings_row, parent, false);
//        }

        final AutoCompleteTextView dumplingNameEditText =
                (AutoCompleteTextView) row.findViewById(R.id.dumplingNameEditText);
        dumplingNameEditText.setAdapter(autocompleteAdapterFactory.createAdapter());
        dumplingNameEditText.addTextChangedListener(autocompleteAdapterFactory
                .createListener((ImageView) row.findViewById(R.id.dumplingImage)));

        TextView dumplingServingCountTextView = (TextView) row.findViewById(R.id.dumplingServingCountTextView);
        SeekBar seekBar = (SeekBar) row.findViewById(R.id.dumplingServingSeekBar);
        DumplingServingsViewHook viewHook = new DumplingServingsViewHook(dumplingServings);
        viewHook.bind(dumplingNameEditText);
        viewHook.bind(seekBar, dumplingServingCountTextView);
        dumplingNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dumplingNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rowWithFocus = position;
                    indexWithFocus = dumplingNameEditText.getSelectionStart();
                }
            }
        });
        if (position == rowWithFocus) {
            dumplingNameEditText.setSelection(indexWithFocus);
            dumplingNameEditText.requestFocus();
        }

        dumplingNameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dumplingNameEditText.clearFocus();
            }
        });
        return row;
    }
}
