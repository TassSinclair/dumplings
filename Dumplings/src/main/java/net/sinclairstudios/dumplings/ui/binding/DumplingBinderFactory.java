package net.sinclairstudios.dumplings.ui.binding;

import android.content.Context;
import android.widget.*;
import net.sinclairstudios.dumplings.domain.DumplingDefaults;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.dumplings.domain.HasDumpling;
import net.sinclairstudios.dumplings.ui.widgets.DumplingNameAutocompleteAdapterFactory;
import net.sinclairstudios.util.TextViewUpdater;

public class DumplingBinderFactory {

    private final DumplingNameAutocompleteAdapterFactory dumplingNameAutocompleteAdapterFactory;
    private final DumplingDefaults dumplingDefaults;

    public DumplingBinderFactory(Context context, DumplingDefaults dumplingDefaults) {
        this.dumplingDefaults = dumplingDefaults;
        this.dumplingNameAutocompleteAdapterFactory =
                new DumplingNameAutocompleteAdapterFactory(context, dumplingDefaults);
    }

    public void bindRatingBar(DumplingRating dumplingRating, RatingBar ratingRatingBar) {
        ratingRatingBar.setOnRatingBarChangeListener(null);
        ratingRatingBar.setProgress(dumplingRating.getRating().getValue());
        ratingRatingBar.setOnRatingBarChangeListener(new DumplingRatingViewHookRatingBar(dumplingRating));
    }

    public void bindListenerTrackingEditText(HasDumpling hasDumpling, AutoCompleteTextView textView) {
        textView.addTextChangedListener(new DumplingNameBinderTextWatcher(hasDumpling));
        textView.setAdapter(dumplingNameAutocompleteAdapterFactory.createAdapter());
        textView.setText(hasDumpling.getDumpling().getName());
    }

    public void bindImageView(TextView textView, ImageView imageView) {
        DumplingImageUpdatingViewTextWatcher textWatcher
                = new DumplingImageUpdatingViewTextWatcher(dumplingDefaults, imageView);
        textView.addTextChangedListener(textWatcher);
        imageView.setImageDrawable(dumplingDefaults.getIcon(textView.getText().toString()));
    }

    public void bindSeekBar(DumplingServings dumplingServings, TextViewUpdater textViewUpdater, SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(null);
        seekBar.setProgress(dumplingServings.getServings());
        DumplingServingChangeListener onSeekBarChangeListener =
                new DumplingServingChangeListener(dumplingServings, textViewUpdater);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        onSeekBarChangeListener.onProgressChanged(seekBar, seekBar.getProgress(), false);
    }
}
