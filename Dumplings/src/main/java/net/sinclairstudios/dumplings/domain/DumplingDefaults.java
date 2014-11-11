package net.sinclairstudios.dumplings.domain;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import net.sinclairstudios.dumplings.BuildConfig;
import net.sinclairstudios.dumplings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DumplingDefaults {

    private final List<String> defaultNames;
    private final List<Drawable> defaultIcons;
    private final Drawable unknownIcon;

    public DumplingDefaults(Context context) {
        defaultNames = Arrays.asList(context.getResources().getStringArray(R.array.defaultDumplingNames));

        defaultIcons = new ArrayList<Drawable>();
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.defaultDumplingIcons);
        if (BuildConfig.DEBUG && typedArray.length() != defaultNames.size()) {
            throw new RuntimeException(
                    "default dumpling icon and default dumpling name arrays must be the same length");
        }
        for (int i = 0; i < typedArray.length(); ++i) {
            defaultIcons.add(typedArray.getDrawable(i));
        }

        unknownIcon = context.getResources().getDrawable(R.drawable.simple_dumpling);
    }

    public List<String> getDefaultNames() {
        return defaultNames;
    }

    public Drawable getIcon(String name) {
        int index = defaultNames.indexOf(name);
        if (index > -1) {
            return defaultIcons.get(index);
        }
        return unknownIcon;
    }
}
