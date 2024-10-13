package net.sinclairstudios.dumplings.domain

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import net.sinclairstudios.dumplings.R

class DumplingDefaults(context: Context) {

    val defaultNames = context.resources.getStringArray(R.array.defaultDumplingNames)
    val defaultIcons = context.resources.obtainTypedArray(R.array.defaultDumplingIcons).let {
        val defaultIcons: MutableList<Drawable> = mutableListOf()
        for (i in 0 until it.length()) {
            defaultIcons.add(it.getDrawable(i)!!)
        }
        return@let defaultIcons
    }
    val unknownIcon = ContextCompat.getDrawable(context, R.drawable.simple_dumpling)!!

    fun getIcon(name: String): Drawable {
        val index = defaultNames.indexOf(name)
        if (index > -1) {
            return defaultIcons[index]
        }
        return unknownIcon
    }
}