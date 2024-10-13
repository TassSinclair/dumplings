package net.sinclairstudios.dumplings.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.R

class ListenerTrackingEditTextView(context: Context, attributeSet: AttributeSet, defStyle: Int) : androidx.appcompat.widget.AppCompatAutoCompleteTextView(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, R.attr.autoCompleteTextViewStyle)

    private var textChangedListeners: MutableList<TextWatcher>? = null

    override fun addTextChangedListener(textWatcher: TextWatcher) {
        super.addTextChangedListener(textWatcher)
        if (textChangedListeners == null) {
            textChangedListeners = mutableListOf<TextWatcher>()
        }
        textChangedListeners?.add(textWatcher)
    }

    fun clearTextChangedListeners() {
        textChangedListeners?.forEach { textWatcher ->
            super.removeTextChangedListener(textWatcher)
        }
        textChangedListeners?.clear()
    }
}