package net.sinclairstudios.android.dumplings.domain

import java.io.Serializable

public open class Rating(val value: Int) : Serializable {
    public override fun toString(): String {
        return "rated " + Integer.toString(value)
    }
}
