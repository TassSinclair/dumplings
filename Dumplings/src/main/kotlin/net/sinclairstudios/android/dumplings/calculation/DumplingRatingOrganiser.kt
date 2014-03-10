package net.sinclairstudios.android.dumplings.calculation


import java.io.Serializable
import net.sinclairstudios.android.dumplings.domain.DumplingServingCalculation
import java.util.ArrayList
import net.sinclairstudios.android.dumplings.domain.Fraction
import net.sinclairstudios.android.dumplings.domain.DumplingRating
import net.sinclairstudios.android.dumplings.domain.DumplingOrder
import java.util.Collections

public class DumplingRatingOrganiser(private val equaliser : DumplingCalculationEqualiser) {

    public fun organise(dumplingRatings : List<DumplingRating>, howManyPeople : Int,
                        preferMultiplesOf : Int) : List<DumplingOrder> {
        val servingMultiplier = Fraction(1, preferMultiplesOf)

        val calculations = transformRatingsToCalculations(dumplingRatings, howManyPeople, servingMultiplier)

        equaliser.equalise(calculations);

        return transformCalculationsToOrders(calculations, servingMultiplier)
    }

    private fun transformRatingsToCalculations(ratings: List<DumplingRating>, howManyPeople: Int,
                                               servingMultiplier: Fraction) : List<DumplingServingCalculation> {
        val totalRatings = ratings.fold(0, {
            runningTotal, rating -> (runningTotal + rating.rating.value)
        })
        return ratings.map({
            rating -> DumplingServingCalculation(rating.dumpling,
                Fraction(rating.rating.value * howManyPeople, totalRatings) * servingMultiplier)
        });
    }

    private fun transformCalculationsToOrders(calculations : List<DumplingServingCalculation>,
                                              servingMultiplier : Fraction) : List<DumplingOrder> {
        return calculations.map({
            calculation -> DumplingOrder(calculation.dumpling,
                                   (calculation.servings / servingMultiplier).getAsInt())
        })
    }
}
