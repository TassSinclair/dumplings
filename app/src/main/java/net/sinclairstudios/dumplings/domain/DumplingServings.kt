package net.sinclairstudios.dumplings.domain

import java.io.Serializable

data class DumplingServings(override var dumpling: Dumpling, var servings: Int) : Serializable, HasDumpling {
    override fun toString() : String {
        return "$dumpling ($servings servings)"
    }
}