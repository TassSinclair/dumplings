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
        val equivalent = all.find({ existing ->
            existing.dumpling == dumplingServings.dumpling
        })

        if (equivalent != null) {
            equivalent.servings = equivalent.servings + dumplingServings.servings
        } else {
            $all.add(dumplingServings)
        }
    }
}
