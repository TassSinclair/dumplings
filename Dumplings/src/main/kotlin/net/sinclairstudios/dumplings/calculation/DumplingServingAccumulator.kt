package net.sinclairstudios.dumplings.calculation

import net.sinclairstudios.dumplings.domain.DumplingServings
import java.util.ArrayList
import net.sinclairstudios.dumplings.domain.DumplingOrder

public class DumplingServingAccumulator {

    private val _all = ArrayList<DumplingServings>()

    public fun getAll(): ArrayList<DumplingOrder> {
        return ArrayList<DumplingOrder>(_all.map { item -> DumplingOrder(item) })
    }

    public fun add(dumplingServingList : List<DumplingServings>) {
        dumplingServingList.forEach({ filterAndUpdateExisting(it) })
    }

    fun filterAndUpdateExisting(dumplingServings : DumplingServings) {
        val equivalent = _all.firstOrNull({ existing ->
            existing.dumpling == dumplingServings.dumpling
        })

        if (equivalent != null) {
            equivalent.servings = equivalent.servings + dumplingServings.servings
        } else if (dumplingServings.servings > 0) {
            _all.add(dumplingServings)
        }
    }
}
