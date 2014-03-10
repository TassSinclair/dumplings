package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DumplingRatingList implements Serializable, Iterable<DumplingRating> {

    private final List<DumplingRating> dumplingRatings;

    public DumplingRatingList() {
        this.dumplingRatings = new ArrayList<DumplingRating>();
    }

    public DumplingRatingList(List<DumplingRating> dumplingRatings) {
        this.dumplingRatings = new ArrayList<DumplingRating>(dumplingRatings);
    }

    public List<DumplingRating> get() {
        return dumplingRatings;
    }

    public DumplingRating get(int index) {
        return dumplingRatings.get(index);
    }

    @Override
    public Iterator<DumplingRating> iterator() {
        return dumplingRatings.iterator();
    }

    public Iterable<DumplingRatingViewHook> getDumplingRatingViewHooks() {
        List<DumplingRatingViewHook> ratingViewHookList = new ArrayList<DumplingRatingViewHook>();
        for (DumplingRating dumplingRating : dumplingRatings) {
            ratingViewHookList.add(new DumplingRatingViewHook(dumplingRating));
        }
        return ratingViewHookList;
    }
}
