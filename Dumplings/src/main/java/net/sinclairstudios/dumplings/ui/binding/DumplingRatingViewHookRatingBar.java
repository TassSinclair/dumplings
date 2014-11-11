package net.sinclairstudios.dumplings.ui.binding;

import android.util.Log;
import android.widget.RatingBar;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.Rating;

public class DumplingRatingViewHookRatingBar implements RatingBar.OnRatingBarChangeListener {

    private final DumplingRating dumplingRating;

    public DumplingRatingViewHookRatingBar(DumplingRating dumplingRating) {
        this.dumplingRating = dumplingRating;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        dumplingRating.setRating(new Rating(ratingBar.getProgress()));
        Log.d(DumplingRatingViewHookRatingBar.class.getName(), "Change rating event received: " + dumplingRating);
    }
}