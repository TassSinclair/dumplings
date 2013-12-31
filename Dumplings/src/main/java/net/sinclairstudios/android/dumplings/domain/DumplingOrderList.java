package net.sinclairstudios.android.dumplings.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DumplingOrderList implements Serializable, Iterable<DumplingOrder> {
    private final List<DumplingOrder> dumplingOrders;

    public DumplingOrderList(DumplingRatingList dumplingRatings, int howManyPeople) {

        List<DumplingServingCalculation> quantities = createDumplingServingCalculations(dumplingRatings,
                howManyPeople);

        List<DumplingServingCalculation> filteredQuantities = filterServingsWithRemainders(quantities);

        while (filteredQuantities.size() > 1) {
            findLowestFraction(filteredQuantities).transferRemainderTo(findHighestFraction(filteredQuantities));
            filteredQuantities = filterServingsWithRemainders(filteredQuantities);
        }

        dumplingOrders = createDumplingOrdersFromCalculations(quantities);
    }

    private List<DumplingOrder> createDumplingOrdersFromCalculations(List<DumplingServingCalculation> quantities) {
        ArrayList<DumplingOrder> orders = new ArrayList<DumplingOrder>();
        for (DumplingServingCalculation quantity : quantities) {
            orders.add(new DumplingOrder(quantity.getDumpling(), quantity.getServings().getAsInt()));
        }
        return orders;
    }

    private List<DumplingServingCalculation> createDumplingServingCalculations(DumplingRatingList dumplingRatings,
                                                                               int howManyPeople) {
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
        return quantities;
    }

    private List<DumplingServingCalculation> filterServingsWithRemainders(List<DumplingServingCalculation>
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

    @Override
    public Iterator<DumplingOrder> iterator() {
        return dumplingOrders.iterator();
    }

    public Iterable<DumplingOrderViewHook> getDumplingOrderViewHooks() {
        List<DumplingOrderViewHook> orderViewHookList = new ArrayList<DumplingOrderViewHook>();
        for (DumplingOrder dumplingOrder : dumplingOrders) {
            orderViewHookList.add(new DumplingOrderViewHook(dumplingOrder));
        }
        return orderViewHookList;
    }
}
