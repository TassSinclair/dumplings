package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;

public class DumplingOrder implements Serializable {
    private Dumpling dumpling;
    private int servings;

    public DumplingOrder(Dumpling dumpling, int servings) {
        this.dumpling = dumpling;
        this.servings = servings;
    }

    public Dumpling getDumpling() {
        return dumpling;
    }

    public int getServings() {
        return servings;
    }

    public void setDumpling(Dumpling dumpling) {
        this.dumpling = dumpling;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public String toString() {
        return dumpling.toString() + " (" + servings + " servings)";
    }
}
