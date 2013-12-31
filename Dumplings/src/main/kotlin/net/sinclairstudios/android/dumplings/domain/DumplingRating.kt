package net.sinclairstudios.android.dumplings.domain

import java.io.Serializable

public class DumplingRating(var dumpling: Dumpling, var rating: Rating) : Serializable {
    public override fun toString(): String {
        return dumpling.toString() + ": " + rating.toString()
    }
}
