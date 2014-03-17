package net.sinclairstudios.dumplings.domain

import java.io.Serializable

public class Rating(val value: Int) : Serializable {

    public override fun toString(): String {
        return "rated ${Integer.toString(value)}"
    }
}
