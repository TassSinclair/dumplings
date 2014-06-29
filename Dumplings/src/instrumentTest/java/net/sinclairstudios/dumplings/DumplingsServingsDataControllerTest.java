package net.sinclairstudios.dumplings;

import android.content.SharedPreferences;
import junit.framework.TestCase;
import net.sinclairstudios.dumplings.domain.Dumpling;
import net.sinclairstudios.dumplings.domain.DumplingServings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DumplingsServingsDataControllerTest extends TestCase {

    private DumplingsServingsDataController dataController;

    @Override
    protected void setUp() throws Exception {
        dataController = new DumplingsServingsDataController();
    }

    public void testPopulatesFromSharedPreferences() {
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        when(mockSharedPreferences.getString(DumplingServings.class.getName(), ""))
                .thenReturn("Potato:2;Pork:12;Pineapple:3");

        dataController.populate(mockSharedPreferences);

        List<DumplingServings> servings = dataController.get();

        assertThat(servings, contains(
                equalTo(new DumplingServings(new Dumpling("Potato"), 2)),
                equalTo(new DumplingServings(new Dumpling("Pork"), 12)),
                equalTo(new DumplingServings(new Dumpling("Pineapple"), 3))
        ));
    }

    public void testDepopulatesToSharedPreferences() {

        ArrayList<DumplingServings> servings = (ArrayList<DumplingServings>) Arrays.asList(
                new DumplingServings(new Dumpling("Potato"), 34),
                new DumplingServings(new Dumpling("Pork"), 2),
                new DumplingServings(new Dumpling("Pineapple"), 0)
        );
        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);

        dataController.set(servings);
        dataController.depopulate(mockEditor);

        verify(mockEditor).putString(DumplingServings.class.getName(),
                "Potato:34;Pork:2;Pineapple:0");
    }
}
