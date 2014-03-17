package net.sinclairstudios.dumplings;

import android.content.SharedPreferences;
import android.content.res.Resources;

import net.sinclairstudios.dumplings.domain.Dumpling;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.Rating;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DumplingsRatingsDataController implements DataController<ArrayList<DumplingRating>> {

    private ArrayList<DumplingRating> dumplingRatings;

    public DumplingsRatingsDataController() {
        reset();
    }

    @Override
    public void reset() {
        this.dumplingRatings = new ArrayList<DumplingRating>();
    }

    @Override
    public ArrayList<DumplingRating> get() {
        return dumplingRatings;
    }

    @Override
    public void set(ArrayList<DumplingRating> it) {
        this.dumplingRatings = it;
    }

    public void populate(Resources resources) {
        String[] names = resources.getStringArray(R.array.defaultDumplingNames);
        int[] ratings = resources.getIntArray(R.array.defaultDumplingRatings);

        populateFromValues(names, ratings);
    }

    private void populateFromValues(String[] names, int[] ratings) {
        this.dumplingRatings = new ArrayList<DumplingRating>();
        for(int i = 0; i < names.length; ++i) {
            dumplingRatings.add(new DumplingRating(new Dumpling(names[i]), new Rating(ratings[i])));
        }
    }

    public void depopulate(SharedPreferences.Editor editor) {
        StringBuilder builder = new StringBuilder();
        for (DumplingRating dumplingRating : dumplingRatings) {
            builder
                    .append(dumplingRating.getDumpling().getName().replace(";", "").replace(":", ""))
                    .append(":")
                    .append(dumplingRating.getRating().getValue())
                    .append(";");
        }
        editor.putString(DumplingRating.class.getName(), builder.toString());
        editor.commit();
    }

    public void populate(SharedPreferences preferences) {
        String preferencesString = preferences.getString(DumplingRating.class.getName(), "");
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
