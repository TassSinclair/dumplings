package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DumplingNameAutocompleteAdapterFactory {
    private final Context context;
    private final DumplingDefaults dumplingDefaults;

    public DumplingNameAutocompleteAdapterFactory(Context context, DumplingDefaults dumplingDefaults) {
        this.context = context;
        this.dumplingDefaults = dumplingDefaults;
    }

    public ArrayAdapter<String> createAdapter() {
        return new ArrayAdapter<String>(context, R.layout.drop_down_dumpling,
                dumplingDefaults.getDefaultNames());
    }
}
