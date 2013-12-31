package net.sinclairstudios.android.dumplings.domain


public class Fraction(val numerator: Int, val denominator: Int) : Comparable<Fraction> {
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

    public fun hasRemainder(): Boolean {
        return getRemainderAsInt() != 0
    }

    public override fun compareTo(other: Fraction): Int {
        assertSameDenominator(other)
        return this.numerator - other.numerator
    }

    private fun assertSameDenominator(that: Fraction): Unit {
        if (this.denominator != that.denominator) {
            throw IllegalArgumentException("fraction denominators must be the same")
        }
    }
}
