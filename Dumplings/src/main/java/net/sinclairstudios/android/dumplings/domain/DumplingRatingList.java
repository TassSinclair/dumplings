package net.sinclairstudios.android.dumplings.domain;

import net.sinclairstudios.android.dumplings.domain.DumplingRating;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingViewHook;

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

    public DumplingRating get(int index) {
        return dumplingRatings.get(index);
    }

    @Override
    public Iterator<DumplingRating> iterator() {
        return dumplingRatings.iterator();
    }

    public int size() {
        return dumplingRatings.size();
    }

    public Iterable<DumplingRatingViewHook> getDumplingRatingViewHooks() {
        List<DumplingRatingViewHook> ratingViewHookList = new ArrayList<DumplingRatingViewHook>();
        for (DumplingRating dumplingRating : dumplingRatings) {
            ratingViewHookList.add(new DumplingRatingViewHook(dumplingRating));
        }
        return ratingViewHookList;
    }
}
