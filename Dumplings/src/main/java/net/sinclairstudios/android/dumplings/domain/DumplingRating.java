package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;

public class DumplingRating implements Serializable {
    private Dumpling dumpling;

    private Rating rating;
    public DumplingRating(Dumpling dumpling, Rating rating) {
        this.dumpling = dumpling;
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public Dumpling getDumpling() {
        return dumpling;
    }

    @Override
    public String toString() {
        return dumpling.toString() + ": " + rating.toString();
    }

    public void setDumpling(Dumpling dumpling) {
        this.dumpling = dumpling;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
