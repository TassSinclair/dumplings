package net.sinclairstudios.android.dumplings.domain

import net.sinclairstudios.android.dumplings.calculation.DumplingRatingOrganiser
import net.sinclairstudios.android.dumplings.calculation.DumplingCalculationEqualiser

public class DumplingOrderListFactory {

    private val dumplingRatingOrganiser = DumplingRatingOrganiser(DumplingCalculationEqualiser())

    public fun createFromDumplingRatings(dumplingRatings : List<DumplingRating>,
                                              numberOfServings: Int, preferMultiplesOf: Int): DumplingOrderList {
        val dumplingRatingList = dumplingRatingOrganiser.organise(dumplingRatings, numberOfServings, preferMultiplesOf)
        return DumplingOrderList(dumplingRatingList)
    }
}