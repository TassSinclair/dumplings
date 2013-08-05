package net.sinclairstudios.android.dumplings.domain;

public class Fraction implements Comparable<Fraction> {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getReal() {
        return this.numerator / this.denominator;
    }

    private int getRemainderAsInt() {
        return this.numerator % this.denominator;
    }

    public Fraction getRemainder() {
        return new Fraction(getRemainderAsInt(), this.denominator);
    }

    public Fraction add(Fraction toAdd) {
        if (toAdd.denominator != this.denominator) {
            throw new IllegalArgumentException();
        }
        return new Fraction(this.numerator + toAdd.numerator, this.denominator);
    }

    public Fraction subtract(Fraction toAdd) {
        if (toAdd.denominator != this.denominator) {
            throw new IllegalArgumentException();
        }
        return new Fraction(this.numerator - toAdd.numerator, this.denominator);
    }

    public boolean hasRemainder() {
        return getRemainderAsInt() != 0;
    }

    @Override
    public int compareTo(Fraction that) {
        if (that.denominator != this.denominator) {
            throw new IllegalArgumentException();
        }
        return this.numerator - that.numerator;
    }
}
