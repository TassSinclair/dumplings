package net.sinclairstudios.android.dumplings.domain;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

public class DumplingRatingViewHook implements TextWatcher, RatingBar.OnRatingBarChangeListener {
    private final DumplingRating dumplingRating;

    public DumplingRatingViewHook(DumplingRating dumplingRating) {
        this.dumplingRating = dumplingRating;
    }

    public void bind(EditText nameEditText, RatingBar ratingRatingBar) {
        nameEditText.setText(dumplingRating.getDumpling().getName());
        nameEditText.addTextChangedListener(this);
        ratingRatingBar.setProgress(dumplingRating.getRating().getValue());
        ratingRatingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

    @Override
    public void afterTextChanged(Editable editable) {
        this.dumplingRating.setDumpling(new Dumpling(editable.toString()));
        Log.d(getClass().getName(), "Change name event received: " + dumplingRating);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        this.dumplingRating.setRating(new Rating(ratingBar.getProgress()));
        Log.d(getClass().getName(), "Change rating event received: " + dumplingRating);
    }
}