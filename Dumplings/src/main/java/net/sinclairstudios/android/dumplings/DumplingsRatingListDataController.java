package net.sinclairstudios.android.dumplings;

import android.content.SharedPreferences;
import android.content.res.Resources;

import net.sinclairstudios.android.dumplings.domain.Dumpling;
import net.sinclairstudios.android.dumplings.domain.DumplingRating;
import net.sinclairstudios.android.dumplings.domain.DumplingRatingList;
import net.sinclairstudios.android.dumplings.domain.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DumplingsRatingListDataController implements DataController<DumplingRatingList> {

    private DumplingRatingList dumplingRatingList;

    public DumplingsRatingListDataController() {
        reset();
    }

    @Override
    public void reset() {
        this.dumplingRatingList = new DumplingRatingList();
    }

    @Override
    public DumplingRatingList get() {
        return dumplingRatingList;
    }

    @Override
    public void set(DumplingRatingList it) {
        this.dumplingRatingList = it;
    }

    public void populate(Resources resources) {
        String[] names = resources.getStringArray(R.array.defaultDumplingNames);
        int[] ratings = resources.getIntArray(R.array.defaultDumplingRatings);

        populateFromValues(names, ratings);
    }

    private void populateFromValues(String[] names, int[] ratings) {
        List<DumplingRating> dumplingRatios = new ArrayList<DumplingRating>();
        for(int i = 0; i < names.length; ++i) {
            dumplingRatios.add(new DumplingRating(new Dumpling(names[i]), new Rating(ratings[i])));
        }
        dumplingRatingList = new DumplingRatingList(dumplingRatios);
    }

    public void depopulate(SharedPreferences.Editor editor) {
        StringBuilder builder = new StringBuilder();
        for (DumplingRating dumplingRating : dumplingRatingList) {
            builder
                    .append(dumplingRating.getDumpling().getName().replace(";", "").replace(":", ""))
                    .append(":")
                    .append(dumplingRating.getRating().getValue())
                    .append(";");
        }
        editor.putString(DumplingRatingList.class.getName(), builder.toString());
        editor.commit();
    }

    public void populate(SharedPreferences preferences) {
        String preferencesString = preferences.getString(DumplingRatingList.class.getName(), "");
        StringTokenizer tokenizer = new StringTokenizer(preferencesString, ";");
        String[] names = new String[tokenizer.countTokens()];
        int[] ratings = new int[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            names[i] = nextToken.substring(0, nextToken.indexOf(':'));
            ratings[i] = Integer.parseInt(nextToken.substring(nextToken.indexOf(':') + 1));
            ++i;
        }
        populateFromValues(names, ratings);
    }
}
