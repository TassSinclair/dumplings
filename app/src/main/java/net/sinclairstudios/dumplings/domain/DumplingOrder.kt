package net.sinclairstudios.dumplings.domain

import java.io.Serializable

class DumplingOrder(var servings: DumplingServings) : Serializable {
    var arrived: Int = 0

    override fun toString() : String {
        return "$servings ($arrived arrived)"
    }
}