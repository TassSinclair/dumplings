package net.sinclairstudios.dumplings.domain
import java.io.Serializable

class DumplingServingCalculation(val dumpling : Dumpling, var servings: Fraction) : Serializable {

    fun trimRemainder() : Fraction {
        val remainder = this.servings.getRemainder()
        this.servings = this.servings.subtract(remainder)
        return remainder
    }

    fun addServings(servings: Fraction) {
        this.servings = this.servings.add(servings)
    }

    public override fun toString() : String {
        return "$dumpling with $servings servings"
    }
}