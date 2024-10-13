package net.sinclairstudios.dumplings.domain

import android.content.SharedPreferences
import android.content.res.Resources
import net.sinclairstudios.dumplings.R
import java.io.Serializable
import java.util.StringTokenizer

class DumplingsServings : Serializable, DataControl<Array<DumplingServings>> {

    private var dumplingsServings: Array<DumplingServings> = arrayOf()

    override fun reset() {
        dumplingsServings = arrayOf()
    }

    override fun get(): Array<DumplingServings> = dumplingsServings

    override fun set(it: Array<DumplingServings>) {
        dumplingsServings = it
    }

    fun populate(resources: Resources) {
        val names = resources.getStringArray(R.array.defaultDumplingNames)
        val servings = resources.getIntArray(R.array.defaultDumplingServings)

        populateFromValues(names, servings)
    }

    private fun populateFromValues(names: Array<String>, servings: IntArray) {
        dumplingsServings = names.mapIndexed { index, name ->
            DumplingServings(
                Dumpling(name), servings[index]
            )
        }.toTypedArray()
    }

    fun depopulate(editor: SharedPreferences.Editor) {
        val builder = StringBuilder()
        for ((dumpling, serving) in dumplingsServings) {
            builder
                .append(dumpling.name.replace(";", "").replace(":", ""))
                .append(":")
                .append(serving)
                .append(";")
        }
        editor.putString(DumplingServings::class.simpleName, builder.toString())
        editor.commit()
    }

    fun populate(preferences: SharedPreferences) {
        val preferencesString = preferences.getString(DumplingServings::class.simpleName, "")
        val tokenizer = StringTokenizer(preferencesString, ";")
        val names = arrayOfNulls<String>(tokenizer.countTokens())
        val servings = IntArray(tokenizer.countTokens())
        var i = 0

        while (tokenizer.hasMoreTokens()) {
            val nextToken = tokenizer.nextToken()
            names[i] = nextToken.substring(0, nextToken.indexOf(':'))
            // Limitation: Only works for integer values less than 10.
            servings[i] = nextToken.substring(nextToken.indexOf(':') + 1).toInt()
            ++i
        }
        populateFromValues(names.filterNotNull().toTypedArray(), servings)
    }
}