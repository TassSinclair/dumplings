package net.sinclairstudios.dumplings.domain

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.util.CountTracker
import net.sinclairstudios.dumplings.util.CountTrackerListener
import net.sinclairstudios.dumplings.util.TextViewUpdater

class DumplingOrderViewHook(private val dumplingOrder: DumplingOrder) {
    fun bind(
        nameTextView: TextView,
        checkboxTableLayout: TableLayout,
        masterCountTracker: CountTracker,
        context: Context
    ) {
        val thisCountTracker: CountTracker = CountTracker(dumplingOrder.servings.servings)
        nameTextView.text = "{} " + dumplingOrder.servings.dumpling.name
        thisCountTracker.addOnAddListener(object : CountTrackerListener {
            val textViewUpdater = TextViewUpdater(nameTextView)
            override fun onCountTrackerAdd(addition: Int, value: Int) {
                textViewUpdater.update(value)
            }

        })
        val onCheckedChangeListener =
            createListener(dumplingOrder, masterCountTracker, thisCountTracker)

        var row: ViewGroup? = null
        checkboxTableLayout.removeAllViews()
        var arrived = dumplingOrder.arrived
        for (i in 0 until dumplingOrder.servings.servings) {
            if (i % 5 == 0) {
                row = LayoutInflater.from(context).inflate(
                    R.layout.your_order_servings_row,
                    checkboxTableLayout, false
                ) as TableRow?
                checkboxTableLayout.addView(row)
            }
            val checkbox = createCheckbox(context, onCheckedChangeListener, arrived > 0)
            --arrived
            row!!.addView(checkbox)
        }
    }

    private fun createListener(
        dumplingOrder: DumplingOrder,
        masterCountTracker: CountTracker,
        thisCountTracker: CountTracker
    ): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
            val progress = (if (checked) 1 else -1)
            Log.d(
                DumplingOrderViewHook::class.simpleName,
                "Change arrival event received: $dumplingOrder, $checked"
            )
            dumplingOrder.arrived = dumplingOrder.arrived + progress
            thisCountTracker.add(-progress)
            masterCountTracker.add(-progress)
        }
    }

    private fun createCheckbox(
        context: Context, onCheckedChangeListener: CompoundButton.OnCheckedChangeListener,
        initialValue: Boolean
    ): CheckBox {
        val checkBox = CheckBox(context)
        checkBox.setButtonDrawable(R.drawable.dumpling_checkbox)
        checkBox.setBackgroundResource(R.drawable.dumpling_checkbox)
        checkBox.isChecked = initialValue
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener)
        return checkBox
    }
}