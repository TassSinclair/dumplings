package net.sinclairstudios.dumplings.domain

import android.content.SharedPreferences
import android.content.res.Resources
import net.sinclairstudios.dumplings.R
import java.io.Serializable
import java.util.StringTokenizer

class DumplingsRatings : Serializable, DataControl<Array<DumplingRating>> {

    private var dumplingsRatings: Array<DumplingRating> = arrayOf()

    override fun reset() {
        dumplingsRatings = arrayOf()
    }

    override fun get(): Array<DumplingRating> = dumplingsRatings

    override fun set(it: Array<DumplingRating>) {
        dumplingsRatings = it
    }

    fun populate(resources: Resources) {
        val names = resources.getStringArray(R.array.defaultDumplingNames)
        val ratings = resources.getIntArray(R.array.defaultDumplingRatings)

        populateFromValues(names, ratings)
    }

    private fun populateFromValues(names: Array<String>, ratings: IntArray) {
        dumplingsRatings = names.mapIndexed { index, name ->
            DumplingRating(
                Dumpling(name), Rating(ratings[index])
            )
        }.toTypedArray()
    }

    fun depopulate(editor: SharedPreferences.Editor) {
        val builder = StringBuilder()
        for ((dumpling, rating) in dumplingsRatings) {
            builder
                .append(dumpling.name.replace(";", "").replace(":", ""))
                .append(":")
                .append(rating.value)
                .append(";")
        }
        editor.putString(DumplingsRatings::class.java.name, builder.toString())
        editor.commit()
    }

    fun populate(preferences: SharedPreferences) {
        val preferencesString = preferences.getString(DumplingRating::class.java.name, "")
        val tokenizer = StringTokenizer(preferencesString, ";")
        val names = arrayOfNulls<String>(tokenizer.countTokens())
        val ratings = IntArray(tokenizer.countTokens())
        var i = 0

        while (tokenizer.hasMoreTokens()) {
            val nextToken = tokenizer.nextToken()
            names[i] = nextToken.substring(0, nextToken.indexOf(':'))
            // Limitation: Only works for integer values less than 10.
            ratings[i] = nextToken.substring(nextToken.indexOf(':') + 1).toInt()
            ++i
        }
        populateFromValues(names.filterNotNull().toTypedArray(), ratings)
    }

}