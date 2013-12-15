package net.sinclairstudios.android.dumplings.domain;

public class Fraction implements Comparable<Fraction> {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getAsInt() {
        return this.numerator / this.denominator;
    }

    private int getRemainderAsInt() {
        return this.numerator % this.denominator;
    }

    public Fraction getRemainder() {
        return new Fraction(getRemainderAsInt(), this.denominator);
    }

    public Fraction add(Fraction that) {
        assertSameDenominator(that);
        return new Fraction(this.numerator + that.numerator, this.denominator);
    }

    public Fraction subtract(Fraction that) {
        assertSameDenominator(that);
        return new Fraction(this.numerator - that.numerator, this.denominator);
    }

    public boolean hasRemainder() {
        return getRemainderAsInt() != 0;
    }

    @Override
    public int compareTo(Fraction that) {
        assertSameDenominator(that);
        return this.numerator - that.numerator;
    }

    private void assertSameDenominator(Fraction that) {
        if (that.denominator != this.denominator) {
            throw new IllegalArgumentException("fraction denominators must be the same");
        }
    }
}
