package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import java.security.interfaces.DSAKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DumplingServingAdapter extends ArrayAdapter<DumplingServings> {

    private final DumplingNameAutocompleteAdapterFactory autocompleteAdapterFactory;
    private int rowWithFocus = -1;
    private int indexWithFocus = -1;
    private ListView listView;

    public DumplingServingAdapter(Context context, List<DumplingServings> dumplingServings) {
        super(context, 0, dumplingServings);
        autocompleteAdapterFactory = new DumplingNameAutocompleteAdapterFactory(context);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Nullable
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        DumplingServings dumplingServings = getItem(position);

        View row = convertView;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.specific_servings_row, parent, false);
        }

        final ListenerTrackingEditText dumplingNameEditText =
                (ListenerTrackingEditText) row.findViewById(R.id.dumplingNameEditText);

        dumplingNameEditText.clearTextChangedListeners();

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
