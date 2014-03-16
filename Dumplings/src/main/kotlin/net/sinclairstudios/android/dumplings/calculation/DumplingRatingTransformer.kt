package net.sinclairstudios.android.dumplings.calculation


import java.io.Serializable
import net.sinclairstudios.android.dumplings.domain.DumplingServingCalculation
import java.util.ArrayList
import net.sinclairstudios.android.dumplings.domain.Fraction
import net.sinclairstudios.android.dumplings.domain.DumplingRating
import net.sinclairstudios.android.dumplings.domain.DumplingServings
import java.util.Collections

public class DumplingRatingTransformer(private val equaliser : DumplingCalculationEqualiser) {

    public fun organise(dumplingRatings : List<DumplingRating>, howManyPeople : Int,
                        preferMultiplesOf : Int) : List<DumplingServings> {
        val servingMultiplier = Fraction(1, preferMultiplesOf)

        val calculations = transformRatingsToCalculations(dumplingRatings, howManyPeople, servingMultiplier)

        equaliser.equalise(calculations);

        return transformCalculationsToOrders(calculations, servingMultiplier)
    }

    private fun transformRatingsToCalculations(ratings: List<DumplingRating>, howManyPeople: Int,
                                               servingMultiplier: Fraction) : List<DumplingServingCalculation> {
        val totalRatings = Math.max(ratings.fold(0, {
            runningTotal, rating -> (runningTotal + rating.rating.value)
        }), 1)
        return ratings.map({
            rating -> DumplingServingCalculation(rating.dumpling,
                Fraction(rating.rating.value * howManyPeople, totalRatings) * servingMultiplier)
        });
    }

    private fun transformCalculationsToOrders(calculations : List<DumplingServingCalculation>,
                                              servingMultiplier : Fraction) : List<DumplingServings> {
        return calculations.map({
            calculation -> DumplingServings(calculation.dumpling,
                                   (calculation.servings / servingMultiplier).getAsInt())
        })
    }
}
