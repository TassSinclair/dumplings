package net.sinclairstudios.dumplings.calculation;

import junit.framework.TestCase;
import net.sinclairstudios.dumplings.domain.*;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static net.sinclairstudios.dumplings.DumplingMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;


public class DumplingRatingTransformerTest extends TestCase {

    private DumplingCalculationEqualiser mockEqualiser;
    private DumplingRatingTransformer transformer;

    @Override
    protected void setUp() throws Exception {
        mockEqualiser = mock(DumplingCalculationEqualiser.class);
        transformer = new DumplingRatingTransformer(mockEqualiser);
    }

    public void testShouldTransformDumplingRatingsIntoDumplingOrders() {
        DumplingRating rating1 = dumplingRatingWithRating(new Rating(5));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(3));

        List<DumplingServings> orders = transformer.organise(Arrays.asList(rating1, rating2), 1, 1);

        assertThat(orders, contains(
                orderFor(rating1.getDumpling()),
                orderFor(rating2.getDumpling())
        ));
    }

    public void testShouldDelegateToEqualiserToEqualiseCalculations() {
        ArgumentCaptor<List> calculationCaptor = forClass(List.class);

        DumplingRating rating1 = dumplingRatingWithRating(new Rating(1));

        transformer.organise(Arrays.asList(rating1), 1, 1);

        verify(mockEqualiser).equalise(calculationCaptor.capture());

        assertThat((List<DumplingServingCalculation>) calculationCaptor.getValue(), contains(
                calculationForDumpling(rating1.getDumpling())
        ));
    }

    public void testShouldCreateSimpleDumplingCalculationFractionsBasedOnFractionsAlone() {
        ArgumentCaptor<List> calculationCaptor = forClass(List.class);

        DumplingRating rating1 = dumplingRatingWithRating(new Rating(5));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(3));

        transformer.organise(Arrays.asList(rating1, rating2), 1, 1);

        verify(mockEqualiser).equalise(calculationCaptor.capture());

        assertThat((List<DumplingServingCalculation>) calculationCaptor.getValue(), contains(
                calculationWithFraction(new Fraction(5, 8)),
                calculationWithFraction(new Fraction(3, 8))
        ));
    }

    public void testShouldMultiplyNumeratorOfDumplingCalculationFractionsBasedOnNumberOfServings() {
        ArgumentCaptor<List> calculationCaptor = forClass(List.class);

        DumplingRating rating1 = dumplingRatingWithRating(new Rating(5));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(3));

        transformer.organise(Arrays.asList(rating1, rating2), 5, 1);

        verify(mockEqualiser).equalise(calculationCaptor.capture());

        assertThat((List<DumplingServingCalculation>) calculationCaptor.getValue(), contains(
                calculationWithFraction(new Fraction(25, 8)),
                calculationWithFraction(new Fraction(15, 8))
        ));
    }

    public void testShouldMultiplyDivisorOfDumplingCalculationFractionsBeforeEqualisingBasedOnPreferredMultiples() {
        ArgumentCaptor<List> calculationCaptor = forClass(List.class);

        DumplingRating rating1 = dumplingRatingWithRating(new Rating(5));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(3));

        transformer.organise(Arrays.asList(rating1, rating2), 1, 2);

        verify(mockEqualiser).equalise(calculationCaptor.capture());

        assertThat((List<DumplingServingCalculation>) calculationCaptor.getValue(), contains(
                calculationWithFraction(new Fraction(5, 16)),
                calculationWithFraction(new Fraction(3, 16))
        ));
    }

    public void testShouldReturnDumplingOrdersBasedOnWholeFractions() {
        DumplingRating rating1 = dumplingRatingWithRating(new Rating(0));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(0));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<DumplingServingCalculation> calculations =
                        (List<DumplingServingCalculation>) invocation.getArguments()[0];
                calculations.get(0).setServings(new Fraction(5, 5));
                calculations.get(1).setServings(new Fraction(10, 5));
                return null;
            }
        }).when(mockEqualiser).equalise(anyList());

        List<DumplingServings> dumplingOrders = transformer.organise(Arrays.asList(rating1, rating2), 1, 1);


        assertThat(dumplingOrders, contains(
                servingsWithServings(1),
                servingsWithServings(2)
        ));
    }

    public void testShouldDivideDumplingFractionsBasedOnPreferredServingsWhenReturningDumplingOrders() {
        DumplingRating rating1 = dumplingRatingWithRating(new Rating(0));
        DumplingRating rating2 = dumplingRatingWithRating(new Rating(0));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<DumplingServingCalculation> calculations =
                        (List<DumplingServingCalculation>) invocation.getArguments()[0];
                calculations.get(0).setServings(new Fraction(5, 15));
                calculations.get(1).setServings(new Fraction(15, 15));
                return null;
            }
        }).when(mockEqualiser).equalise(anyList());

        List<DumplingServings> dumplingOrders = transformer.organise(Arrays.asList(rating1, rating2), 1, 3);


        assertThat(dumplingOrders, contains(
                servingsWithServings(1),
                servingsWithServings(3)
        ));
    }

    private DumplingRating dumplingRatingWithRating(Rating rating) {
        return new DumplingRating(mock(Dumpling.class), rating);
    }
}
