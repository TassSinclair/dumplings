package net.sinclairstudios.android.dumplings.calculation


import java.io.Serializable
import net.sinclairstudios.android.dumplings.domain.DumplingServingCalculation
import java.util.ArrayList
import net.sinclairstudios.android.dumplings.domain.Fraction

public open class DumplingCalculationEqualiser {

    public open fun equalise(dumplingServingCalculations : List<DumplingServingCalculation>) {

        var filteredQuantities = filterCalculationsWithRemainders(dumplingServingCalculations)
        while (filteredQuantities.size() > 1)
        {
            val remainder = findLowestRemainder(filteredQuantities).trimRemainder()
            findHighestRemainder(filteredQuantities).addServings(remainder)

            filteredQuantities = filterCalculationsWithRemainders(filteredQuantities)
        }
    }

    private fun filterCalculationsWithRemainders(calculations: List<DumplingServingCalculation>) :
            List<DumplingServingCalculation> {
        return calculations.filter({ calculation -> calculation.servings.hasRemainder()});
    }

    private fun findLowestRemainder(calculations: List<DumplingServingCalculation>) : DumplingServingCalculation {
        return calculations.minBy({ quantity -> quantity.servings.getRemainder() }) ?: calculations.first()
    }

    private fun findHighestRemainder(calculations: List<DumplingServingCalculation>) : DumplingServingCalculation {
        return calculations.maxBy({ quantity -> quantity.servings.getRemainder() }) ?: calculations.first()
    }
}
