package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;

public class Rating implements Serializable {
    private final int value;

    public Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "rated " + Integer.toString(value);
    }
}
