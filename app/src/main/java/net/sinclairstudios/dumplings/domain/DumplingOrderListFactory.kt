package net.sinclairstudios.dumplings.domain

import net.sinclairstudios.dumplings.calculation.DumplingCalculationEqualiser
import net.sinclairstudios.dumplings.calculation.DumplingRatingTransformer

class DumplingOrderListFactory {
    private val dumplingRatingOrganiser = DumplingRatingTransformer(DumplingCalculationEqualiser())

    public fun createFromDumplingRatings(dumplingRatings : List<DumplingRating>, numberOfServings: Int,
                                         preferMultiplesOf: Int): List<DumplingServings> {
        return dumplingRatingOrganiser.organise(dumplingRatings, numberOfServings, preferMultiplesOf)
    }
}