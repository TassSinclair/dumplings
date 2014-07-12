package net.sinclairstudios.dumplings.calculation

import net.sinclairstudios.dumplings.domain.DumplingServings
import java.util.ArrayList

public class DumplingServingAccumulator {

    val all = ArrayList<DumplingServings>()
    get() = ArrayList<DumplingServings>($all)

    public fun add(dumplingServingList : List<DumplingServings>) {
        dumplingServingList.forEach({ filterAndUpdateExisting(it) })
    }

    fun filterAndUpdateExisting(dumplingServings : DumplingServings) {
        val equivalent = all.firstOrNull({ existing ->
            existing.dumpling == dumplingServings.dumpling
        })

        if (equivalent != null) {
            equivalent.servings = equivalent.servings + dumplingServings.servings
        } else if (dumplingServings.servings > 0) {
            $all.add(dumplingServings)
        }
    }
}
