package net.sinclairstudios.dumplings.domain

import java.io.Serializable

class Rating(val value: Int) : Serializable {

    override fun toString(): String {
        return "rated ${value.toString()}"
    }
}
