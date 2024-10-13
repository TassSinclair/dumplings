package net.sinclairstudios.dumplings.domain

import java.io.Serializable

data class Dumpling(val name: String): Serializable {

    public override fun toString(): String {
        return "$name dumpling"
    }
}
