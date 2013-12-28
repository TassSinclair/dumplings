package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DumplingOrderList implements Serializable, Iterable<DumplingOrder> {
    private final List<DumplingOrder> dumplingOrders;

    public DumplingOrderList(DumplingRatingList dumplingRatings, int howManyPeople) {
        int totalRatings = 0;
        for (DumplingRating dumplingRating : dumplingRatings) {
            totalRatings += dumplingRating.getRating().getValue();
        }

        List<DumplingServingCalculation> quantities = new ArrayList<DumplingServingCalculation>();

        for (DumplingRating dumplingRating : dumplingRatings) {
            Fraction calculatedServings =
                    new Fraction(dumplingRating.getRating().getValue() * howManyPeople, totalRatings);
            quantities.add(new DumplingServingCalculation(dumplingRating.getDumpling(), calculatedServings));
        }

        List<DumplingServingCalculation> filteredQuantities = filterServingsWithoutRemainders(quantities);

        while (filteredQuantities.size() > 1) {
            DumplingServingCalculation highestFraction = findHighestFraction(filteredQuantities);
            DumplingServingCalculation lowestFraction = findLowestFraction(filteredQuantities);

            highestFraction.addServings(lowestFraction.getServings().getRemainder());
            lowestFraction.subtractServings(lowestFraction.getServings().getRemainder());

            filteredQuantities = filterServingsWithoutRemainders(filteredQuantities);
        }

        dumplingOrders = new ArrayList<DumplingOrder>();
        for (DumplingServingCalculation quantity : quantities) {
            dumplingOrders.add(new DumplingOrder(quantity.getDumpling(), quantity.getServings().getAsInt()));
        }
    }

    private final static class DumplingServingCalculation {
        private final Dumpling dumpling;
        private Fraction servings;

        public DumplingServingCalculation(Dumpling dumpling, Fraction servings) {
            this.dumpling = dumpling;
            setServings(servings);
        }

        public void setServings(Fraction servings) {
            this.servings = servings;
        }

        public void addServings(Fraction toAdd) {
            setServings(servings.add(toAdd));
        }

        public void subtractServings(Fraction toAdd) {
            setServings(servings.subtract(toAdd));
        }

        private Fraction getServings() {
            return servings;
        }

        private Dumpling getDumpling() {
            return dumpling;
        }
    }

    private List<DumplingServingCalculation> filterServingsWithoutRemainders(List<DumplingServingCalculation>
                                                                                     unfilteredServings) {
        List<DumplingServingCalculation> filteredServings = new ArrayList<DumplingServingCalculation>();
        for (DumplingServingCalculation unfilteredServing : unfilteredServings) {
            if (unfilteredServing.getServings().hasRemainder()) {
                filteredServings.add(unfilteredServing);
            }
        }
        return filteredServings;
    }

    private DumplingServingCalculation findLowestFraction(List<DumplingServingCalculation> quantities) {
        assert quantities.size() > 2;

        Iterator<DumplingServingCalculation> iterator = quantities.iterator();

        DumplingServingCalculation lowest = iterator.next();

        while (iterator.hasNext()) {
            DumplingServingCalculation next = iterator.next();
            if (lowest.getServings().getRemainder().compareTo(next.getServings().getRemainder()) > 0) {
                lowest = next;
            }
        }
        assert lowest.getServings().hasRemainder();
        return lowest;
    }

    private DumplingServingCalculation findHighestFraction(List<DumplingServingCalculation> quantities) {
        assert quantities.size() > 2;

        Iterator<DumplingServingCalculation> iterator = quantities.iterator();

        DumplingServingCalculation highest = iterator.next();

        while (iterator.hasNext()) {
            DumplingServingCalculation next = iterator.next();
            if (highest.getServings().getRemainder().compareTo(next.getServings().getRemainder()) < 0) {
                highest = next;
            }
        }
        assert highest.getServings().hasRemainder();
        return highest;
    }

    public DumplingOrder get(int index) {
        return dumplingOrders.get(index);
    }

    @Override
    public Iterator<DumplingOrder> iterator() {
        return dumplingOrders.iterator();
    }

    public int size() {
        return dumplingOrders.size();
    }

    public Iterable<DumplingOrderViewHook> getDumplingOrderViewHooks() {
        List<DumplingOrderViewHook> orderViewHookList = new ArrayList<DumplingOrderViewHook>();
        for (DumplingOrder dumplingOrder : dumplingOrders) {
            orderViewHookList.add(new DumplingOrderViewHook(dumplingOrder));
        }
        return orderViewHookList;
    }
}
