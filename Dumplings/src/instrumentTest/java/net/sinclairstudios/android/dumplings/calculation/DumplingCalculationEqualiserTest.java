package net.sinclairstudios.android.dumplings.calculation;

import junit.framework.TestCase;
import net.sinclairstudios.android.dumplings.domain.Dumpling;
import net.sinclairstudios.android.dumplings.domain.DumplingServingCalculation;
import net.sinclairstudios.android.dumplings.domain.Fraction;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.*;


public class DumplingCalculationEqualiserTest extends TestCase {

    private DumplingCalculationEqualiser equaliser;

    @Override
    protected void setUp() throws Exception {
        equaliser = new DumplingCalculationEqualiser();
    }

    public void testShouldReturnTwoEqualisedDumplingServingCalculationsWhenTheyMatchExpectedMultiples() {

        DumplingServingCalculation calculation1 = dumplingServingCalculationWithFraction(new Fraction(1, 1));
        DumplingServingCalculation calculation2 = dumplingServingCalculationWithFraction(new Fraction(1, 1));

        equaliser.equalise(Arrays.asList(calculation1, calculation2));

        assertThat(calculation1.getServings(), is(equalTo(new Fraction(1, 1))));
        assertThat(calculation2.getServings(), is(equalTo(new Fraction(1, 1))));
    }

    public void testShouldEqualiseTwoDumplingsWithRemaindersEquallingAWholeNumber() {
        DumplingServingCalculation calculation1 = dumplingServingCalculationWithFraction(new Fraction(5, 4));
        DumplingServingCalculation calculation2 = dumplingServingCalculationWithFraction(new Fraction(3, 4));

        equaliser.equalise(Arrays.asList(calculation1, calculation2));

        assertThat(calculation1.getServings(), is(equalTo(new Fraction(4, 4))));
        assertThat(calculation2.getServings(), is(equalTo(new Fraction(4, 4))));
    }

    public void testShouldAttemptToEqualiseDumplingsWithRemaindersNotEquallingAWholeNumber() {
        // From a pen-and-paper example of when the preferred serving multiple is 3
        DumplingServingCalculation calculation1 = dumplingServingCalculationWithFraction(new Fraction(1, 6));
        DumplingServingCalculation calculation3 = dumplingServingCalculationWithFraction(new Fraction(3, 6));
        DumplingServingCalculation calculation5 = dumplingServingCalculationWithFraction(new Fraction(5, 6));
        DumplingServingCalculation calculation6 = dumplingServingCalculationWithFraction(new Fraction(6, 6));

        equaliser.equalise(Arrays.asList(calculation1, calculation3, calculation5,calculation6));

        assertThat(calculation1.getServings(), is(equalTo(new Fraction(0, 6))));
        assertThat(calculation3.getServings(), is(equalTo(new Fraction(3, 6))));
        assertThat(calculation5.getServings(), is(equalTo(new Fraction(6, 6))));
        assertThat(calculation6.getServings(), is(equalTo(new Fraction(6, 6))));
    }

    private DumplingServingCalculation dumplingServingCalculationWithFraction(Fraction fraction) {
        return new DumplingServingCalculation(mock(Dumpling.class), fraction);
    }
}
