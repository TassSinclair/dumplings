package net.sinclairstudios.dumplings.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.activity.DumplingNameDialogFragment
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingServings
import net.sinclairstudios.dumplings.util.TextViewUpdater

class DumplingServingAdapter(
    private val dumplingDefaults: DumplingDefaults,
    fa: FragmentActivity,
    dumplingServings: List<DumplingServings>
) : ArrayAdapter<DumplingServings>(fa, 0, dumplingServings) {

    private val dumplingBinderFactory = DumplingBinderFactory(fa, dumplingDefaults)
    private val fragmentManager = fa.supportFragmentManager
    private val textViewUpdaters = hashMapOf<View, TextViewUpdater>()

    override fun getView(position: Int, toBeRecycled: View?, parent: ViewGroup): View {
        val dumplingServings = getItem(position)!!
        val row = createOrRecycleView(parent, toBeRecycled)
        val dumplingServingCountTextView = row.findViewById<View>(R.id.dumplingServingCountTextView) as TextView
        val dumplingNameTextView = row.findViewById<View>(R.id.dumplingNameTextView) as TextView
        val dumplingNameButton = row.findViewById<View>(R.id.dumplingNameButton) as ImageButton
        val dumplingServingSeekBar = row.findViewById<View>(R.id.dumplingServingSeekBar) as SeekBar

        dumplingNameTextView.text = dumplingServings.dumpling.name

        (row.findViewById<View>(R.id.dumplingImage) as ImageView).setImageDrawable(
            dumplingDefaults.getIcon(dumplingServings.dumpling.name)
        )

        val textViewUpdater = textViewUpdaters.getOrPut(row) {
            TextViewUpdater(dumplingServingCountTextView)
        }

        dumplingBinderFactory.bindSeekBar(dumplingServings, textViewUpdater, dumplingServingSeekBar)

        dumplingNameButton.setOnClickListener(
            DumplingNameDialogFragment.Factory(
                fragmentManager,
                dumplingBinderFactory,
                dumplingServings
            )
        )
        return row
    }

    private fun createOrRecycleView(parent: ViewGroup, toBeRecycled: View?): View {
        if (toBeRecycled == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflater.inflate(R.layout.specific_servings_row, parent, false)
        }
        return toBeRecycled
    }
}