package net.sinclairstudios.dumplings.util

import android.widget.TextView
import java.text.DecimalFormat

class TextViewUpdater(private val textView: TextView) {
    private val fieldValue = textView.text.toString()

    fun update(value: Int) {
        textView.text = fieldValue.replace("{}", DecimalFormat.getInstance().format(value.toLong()))
    }
}