package net.sinclairstudios.dumplings.domain

import java.io.Serializable

public class DumplingRating(override var dumpling: Dumpling, var rating: Rating) : Serializable, HasDumpling {

    public override fun toString(): String {
        return "${dumpling.toString()}: ${rating.toString()}"
    }
}
