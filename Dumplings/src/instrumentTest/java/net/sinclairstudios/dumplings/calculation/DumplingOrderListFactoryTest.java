package net.sinclairstudios.dumplings.calculation;

import junit.framework.TestCase;
import net.sinclairstudios.dumplings.domain.Dumpling;
import net.sinclairstudios.dumplings.domain.DumplingOrderListFactory;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;

public class DumplingOrderListFactoryTest extends TestCase {

    public void testShouldNotLimitDumplingServingsWhenLessThanServingLimit() {
        DumplingOrderListFactory factory = new DumplingOrderListFactory();

        DumplingServings servings1 = createServingsWithServings(5);
        DumplingServings servings2 = createServingsWithServings(5);

        List<DumplingServings> limitedServings = factory.limitDumplingOrders(Arrays.asList(servings1, servings2), 10);

        assertThat(limitedServings, contains(
                servingsWithServings(5),
                servingsWithServings(5)
        ));
    }

    public void testShouldLimitDumplingServingsWhenLessThanServingLimit() {
        DumplingOrderListFactory factory = new DumplingOrderListFactory();

        DumplingServings servings1 = createServingsWithServings(5);
        DumplingServings servings2 = createServingsWithServings(5);

        List<DumplingServings> limitedServings = factory.limitDumplingOrders(Arrays.asList(servings1, servings2), 8);

        assertThat(limitedServings, contains(
                servingsWithServings(5),
                servingsWithServings(3)
        ));
    }

    public void testShouldLimitAndRemoveDumplingServingsWhenLessThanServingLimit() {
        DumplingOrderListFactory factory = new DumplingOrderListFactory();

        DumplingServings servings1 = createServingsWithServings(5);
        DumplingServings servings2 = createServingsWithServings(5);

        List<DumplingServings> limitedServings = factory.limitDumplingOrders(Arrays.asList(servings1, servings2), 4);

        assertThat(limitedServings, contains(
                servingsWithServings(4)
        ));
    }

    private Matcher<DumplingServings> servingsWithServings(final int servings) {
        return new CustomTypeSafeMatcher<DumplingServings>("Servings with servings " + servings) {
            @Override
            protected boolean matchesSafely(DumplingServings dumplingServings) {
                return dumplingServings.getServings() == servings;
            }
        };
    }

    private DumplingServings createServingsWithServings(int servings) {
        return new DumplingServings(mock(Dumpling.class), servings);
    }
}
