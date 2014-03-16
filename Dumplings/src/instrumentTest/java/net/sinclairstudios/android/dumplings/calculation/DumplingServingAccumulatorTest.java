package net.sinclairstudios.android.dumplings.calculation;

import junit.framework.TestCase;
import net.sinclairstudios.android.dumplings.domain.Dumpling;
import net.sinclairstudios.android.dumplings.domain.DumplingServings;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.mock;

public class DumplingServingAccumulatorTest extends TestCase {

    public void testShouldAccumulateMultipleListsOfDumplingServingsAndReturnThem() {
        DumplingServingAccumulator accumulator = new DumplingServingAccumulator();
        DumplingServings servings1 = createServingsWithNameAndServings("1", 0);
        DumplingServings servings2 = createServingsWithNameAndServings("2", 0);
        DumplingServings servings3 = createServingsWithNameAndServings("3", 0);
        DumplingServings servings4 = createServingsWithNameAndServings("4", 0);

        accumulator.add(Arrays.asList(servings1, servings2));
        accumulator.add(Arrays.asList(servings3, servings4));
        List<DumplingServings> all = accumulator.getAll();

        assertThat(all, hasItems(servings1, servings2, servings3,
                servings4));
    }

    public void testShouldCombineServingsWithTheSameDumplingName() {
        DumplingServingAccumulator accumulator = new DumplingServingAccumulator();
        DumplingServings servings1 = createServingsWithNameAndServings("1", 1);
        DumplingServings servings2 = createServingsWithNameAndServings("2", 2);
        DumplingServings servings3 = createServingsWithNameAndServings("1", 3);
        DumplingServings servings4 = createServingsWithNameAndServings("2", 4);

        accumulator.add(Arrays.asList(servings1));
        accumulator.add(Arrays.asList(servings2, servings3, servings4));
        List<DumplingServings> all = accumulator.getAll();

        assertThat(all, hasItems(
                servingsWithNameAndServings("1", 4),
                servingsWithNameAndServings("2", 6)
        ));
    }

    private Matcher<DumplingServings> servingsWithNameAndServings(final String name, final int servings) {
        return new CustomTypeSafeMatcher<DumplingServings>("Dumpling " + name + " with " + servings + " servings") {
            @Override
            protected boolean matchesSafely(DumplingServings dumplingServings) {
                return dumplingServings.getDumpling().getName().equals(name)
                        && dumplingServings.getServings() == servings;
            }
        };
    }

    private DumplingServings createServingsWithNameAndServings(String dumplingName, int servings) {
        return new DumplingServings(new Dumpling(dumplingName), servings);
    }
}
