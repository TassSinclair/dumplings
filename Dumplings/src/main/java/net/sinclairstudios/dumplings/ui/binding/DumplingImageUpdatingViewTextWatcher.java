package net.sinclairstudios.dumplings.ui.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import net.sinclairstudios.dumplings.domain.Dumpling;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.Rating;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import net.sinclairstudios.dumplings.ui.widgets.ListenerTrackingEditText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DumplingImageUpdatingViewTextWatcher implements TextWatcher {

    private final DumplingDefaults dumplingDefaults;
    private final ImageView imageView;

    public DumplingImageUpdatingViewTextWatcher(DumplingDefaults dumplingDefaults, ImageView imageView) {
        this.dumplingDefaults = dumplingDefaults;
        this.imageView = imageView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(@NotNull CharSequence s, int start, int before, int count) {
        imageView.setImageDrawable(dumplingDefaults.getIcon(s.toString()));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}

