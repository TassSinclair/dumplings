package net.sinclairstudios.android.dumplings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import net.sinclairstudios.android.dumplings.domain.Dumpling;
import net.sinclairstudios.android.dumplings.domain.DumplingRating;
import net.sinclairstudios.android.dumplings.domain.DumplingServings;
import net.sinclairstudios.android.dumplings.domain.Rating;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DumplingsServingsDataController implements DataController<ArrayList<DumplingServings>> {

    private ArrayList<DumplingServings> dumplingsServingsList;

    public DumplingsServingsDataController() {
        reset();
    }

    @Override
    public void reset() {
        this.dumplingsServingsList = new ArrayList<DumplingServings>();
    }

    @Override
    public ArrayList<DumplingServings> get() {
        return dumplingsServingsList;
    }

    @Override
    public void set(ArrayList<DumplingServings> it) {
        this.dumplingsServingsList = it;
    }

    public void populate(Resources resources) {
        String[] names = resources.getStringArray(R.array.defaultDumplingNames);
        int[] servings = resources.getIntArray(R.array.defaultDumplingServings);

        populateFromValues(names, servings);
    }

    private void populateFromValues(String[] names, int[] servings) {
        this.dumplingsServingsList = new ArrayList<DumplingServings>();
        for(int i = 0; i < names.length; ++i) {
            dumplingsServingsList.add(new DumplingServings(new Dumpling(names[i]), servings[i]));
        }
    }

    public void depopulate(SharedPreferences.Editor editor) {
        StringBuilder builder = new StringBuilder();
        for (DumplingServings dumplingServings : dumplingsServingsList) {
            builder
                    .append(dumplingServings.getDumpling().getName().replace(";", "").replace(":", ""))
                    .append(":")
                    .append(dumplingServings.getServings())
                    .append(";");
        }
        editor.putString(DumplingServings.class.getName(), builder.toString());
        editor.commit();
    }

    public void populate(SharedPreferences preferences) {
        String preferencesString = preferences.getString(DumplingServings.class.getName(), "");
        StringTokenizer tokenizer = new StringTokenizer(preferencesString, ";");
        String[] names = new String[tokenizer.countTokens()];
        int[] ratings = new int[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            names[i] = nextToken.substring(0, nextToken.indexOf(':'));
            // Limitation: Only works for integer values less than 10.
            ratings[i] = Integer.parseInt(nextToken.substring(nextToken.indexOf(':') + 1));
            ++i;
        }
        populateFromValues(names, ratings);
    }
}
