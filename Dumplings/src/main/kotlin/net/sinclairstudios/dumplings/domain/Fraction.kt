package net.sinclairstudios.dumplings.domain


public open class Fraction(val numerator: Int, val denominator: Int) : Comparable<Fraction> {

    public fun getAsInt(): Int {
        return this.numerator / this.denominator
    }

    private fun getRemainderAsInt(): Int {
        return this.numerator % this.denominator
    }

    public fun getRemainder(): Fraction {
        return Fraction(getRemainderAsInt(), this.denominator)
    }

    public fun add(that: Fraction): Fraction {
        assertSameDenominator(that)
        return Fraction(this.numerator + that.numerator, this.denominator)
    }

    public fun subtract(that: Fraction): Fraction {
        assertSameDenominator(that)
        return Fraction(this.numerator - that.numerator, this.denominator)
    }

    public fun times(that: Fraction): Fraction {
        return Fraction(this.numerator * that.numerator, this.denominator * that.denominator)
    }

    public fun div(that: Fraction): Fraction {
        return Fraction(this.numerator / that.numerator, this.denominator / that.denominator)
    }

    public open fun hasRemainder(): Boolean {
        return getRemainderAsInt() != 0
    }

    public fun toString(): String {
        return "$numerator/$denominator"
    }

    public fun equals(that: Any?): Boolean {
        return that is Fraction
            && equals(that)
    }

    public fun equals(that: Fraction): Boolean {
        return this.numerator == that.numerator
            && this.denominator == that.denominator
    }

    public override fun compareTo(other: Fraction): Int {
        assertSameDenominator(other)
        return this.numerator - other.numerator
    }

    private fun assertSameDenominator(that: Fraction) {
        if (this.denominator != that.denominator) {
            throw IllegalArgumentException("fraction denominators must be the same")
        }
    }
}
