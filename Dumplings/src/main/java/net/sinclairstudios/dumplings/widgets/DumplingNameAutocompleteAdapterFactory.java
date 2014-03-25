package net.sinclairstudios.dumplings.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import net.sinclairstudios.dumplings.R;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DumplingNameAutocompleteAdapterFactory {

    private final Context context;
    private final List<String> defaultDumplingNames;
    private final TypedArray defaultDumplingIcons;

    public DumplingNameAutocompleteAdapterFactory(Context context) {
        this.context = context;
        defaultDumplingNames = Arrays.asList(context.getResources().getStringArray(R.array.defaultDumplingNames));
        this.defaultDumplingIcons = context.getResources().obtainTypedArray(R.array.defaultDumplingIcons);
    }

    public ArrayAdapter<String> createAdapter() {
        return new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, defaultDumplingNames);
    }

    public TextWatcher createListener(final ImageView dumplingImageView) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(@NotNull CharSequence s, int start, int before, int count) {
                updateDumplingImageFromLabel(s.toString(), dumplingImageView);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    public void updateDumplingImageFromLabel(String label, ImageView imageView) {
        int index = defaultDumplingNames.indexOf(label);
        if (index > -1) {
            imageView.setImageDrawable(defaultDumplingIcons.getDrawable(index));
        }
        else {
            imageView.setImageResource(R.drawable.dumpling);
        }
    }
}
