package net.sinclairstudios.android.dumplings.domain;

public final class DumplingServingCalculation {
    private final Dumpling dumpling;
    private Fraction servings;

    public DumplingServingCalculation(Dumpling dumpling, Fraction servings) {
        this.dumpling = dumpling;
        setServings(servings);
    }

    private void setServings(Fraction servings) {
        this.servings = servings;
    }

    public void transferRemainderTo(DumplingServingCalculation remainderRecipient) {
        Fraction remainder = servings.getRemainder();

        remainderRecipient.setServings(remainderRecipient.getServings().add(remainder));
        setServings(getServings().subtract(remainder));
    }

    public Fraction getServings() {
        return servings;
    }

    public Dumpling getDumpling() {
        return dumpling;
    }
}