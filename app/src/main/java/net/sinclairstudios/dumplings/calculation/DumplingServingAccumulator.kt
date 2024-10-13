package net.sinclairstudios.dumplings.calculation

import net.sinclairstudios.dumplings.domain.DumplingOrder
import net.sinclairstudios.dumplings.domain.DumplingServings
import java.util.ArrayList

class DumplingServingAccumulator {
    private val all = mutableListOf<DumplingServings>()

    fun getOrders() = all.map(::DumplingOrder)

    fun add(dumplingServings: List<DumplingServings>) {
        dumplingServings.forEach(::filterAndUpdateExisting)
    }

    private fun filterAndUpdateExisting(dumplingServings : DumplingServings) {
        val equivalent = all.firstOrNull { it.dumpling == dumplingServings.dumpling }

        if (equivalent != null) {
            val replacement = DumplingServings(equivalent.dumpling, dumplingServings.servings + equivalent.servings)
            all[all.indexOf(equivalent)] = replacement
        } else if (dumplingServings.servings > 0) {
            all.add(dumplingServings)
        }
    }
}