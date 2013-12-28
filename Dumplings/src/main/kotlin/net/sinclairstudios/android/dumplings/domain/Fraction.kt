package net.sinclairstudios.android.dumplings.domain


public open class Fraction(val numerator: Int, val denominator: Int) : Comparable<Fraction> {
    public open fun getAsInt(): Int {
        return this.numerator / this.denominator
    }

    private fun getRemainderAsInt(): Int {
        return this.numerator % this.denominator
    }

    public open fun getRemainder(): Fraction {
        return Fraction(getRemainderAsInt(), this.denominator)
    }

    public open fun add(that: Fraction): Fraction {
        assertSameDenominator(that)
        return Fraction(this.numerator + that.numerator, this.denominator)
    }

    public open fun subtract(that: Fraction): Fraction {
        assertSameDenominator(that)
        return Fraction(this.numerator - that.numerator, this.denominator)
    }

    public open fun hasRemainder(): Boolean {
        return getRemainderAsInt() != 0
    }

    public override fun compareTo(other: Fraction): Int {
        assertSameDenominator(other)
        return this.numerator - other.numerator
    }

    private fun assertSameDenominator(that: Fraction): Unit {
        if (that.denominator != this.denominator)
        {
            throw IllegalArgumentException("fraction denominators must be the same")
        }
    }
}
