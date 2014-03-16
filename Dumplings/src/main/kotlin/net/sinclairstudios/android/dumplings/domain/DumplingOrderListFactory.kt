package net.sinclairstudios.android.dumplings.domain

import net.sinclairstudios.android.dumplings.calculation.DumplingRatingTransformer
import net.sinclairstudios.android.dumplings.calculation.DumplingCalculationEqualiser

public class DumplingOrderListFactory {

    private val dumplingRatingOrganiser = DumplingRatingTransformer(DumplingCalculationEqualiser())

    public fun createFromDumplingRatings(dumplingRatings : List<DumplingRating>,
                                              numberOfServings: Int, preferMultiplesOf: Int): List<DumplingServings> {
        return dumplingRatingOrganiser.organise(dumplingRatings, numberOfServings, preferMultiplesOf)
    }

    public fun limitDumplingOrders(dumplingServings : List<DumplingServings>,
                                   numberOfServings: Int) : List<DumplingServings> {
        var numberOfServingsRemaining = numberOfServings
        return dumplingServings.map({ item ->
            val limitedServings = Math.min(numberOfServingsRemaining, item.servings)
            numberOfServingsRemaining -= item.servings
            DumplingServings(item.dumpling, limitedServings)
        }).filter { item ->
            item.servings > 0
        }
    }
}