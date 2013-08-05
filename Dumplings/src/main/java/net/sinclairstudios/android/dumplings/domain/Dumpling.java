package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;

public class Dumpling implements Serializable {
    private final String name;

    public Dumpling(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " dumpling";
    }
}
