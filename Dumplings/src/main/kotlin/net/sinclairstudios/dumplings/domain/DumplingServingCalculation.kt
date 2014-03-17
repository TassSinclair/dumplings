package net.sinclairstudios.dumplings.domain
import java.io.Serializable

public open class DumplingServingCalculation(val dumpling : Dumpling, servings: Fraction) : Serializable {

    open var servings : Fraction = servings

    public open fun trimRemainder() : Fraction {
        val remainder = this.servings.getRemainder()
        this.servings = this.servings.subtract(remainder)
        return remainder
    }

    public open fun addServings(servings: Fraction) {
        this.servings = this.servings.add(servings)
    }

    public override fun toString() : String {
        return "$dumpling with $servings servings"
    }
}