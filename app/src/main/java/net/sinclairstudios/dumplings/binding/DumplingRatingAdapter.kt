package net.sinclairstudios.dumplings.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.activity.DumplingNameDialogFragment
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingRating

class DumplingRatingAdapter(
    private val dumplingDefaults: DumplingDefaults,
    fa: FragmentActivity,
    dumplingRatings: List<DumplingRating>
) : ArrayAdapter<DumplingRating>(fa, 0, dumplingRatings) {

    private val dumplingBinderFactory = DumplingBinderFactory(fa, dumplingDefaults)
    private val fragmentManager = fa.supportFragmentManager

    override fun getView(position: Int, toBeRecycled: View?, parent: ViewGroup): View {
        val dumplingRating = getItem(position)!!
        val row = createOrRecycleView(parent, toBeRecycled)
        val dumplingNameTextView = row.findViewById<View>(R.id.dumplingNameTextView) as TextView
        val dumplingNameButton = row.findViewById<View>(R.id.dumplingNameButton) as ImageButton
        val dumplingRatingBar = row.findViewById<View>(R.id.dumplingRatioRatingBar) as RatingBar

        dumplingNameTextView.text = dumplingRating.dumpling.name
        (row.findViewById<View>(R.id.dumplingImage) as ImageView).setImageDrawable(
            dumplingDefaults.getIcon(dumplingRating.dumpling.name)
        )

        dumplingBinderFactory.bindRatingBar(dumplingRating, dumplingRatingBar)

        dumplingNameButton.setOnClickListener(
            DumplingNameDialogFragment.Factory(
                fragmentManager,
                dumplingBinderFactory,
                dumplingRating
            )
        )
        return row
    }

    private fun createOrRecycleView(parent: ViewGroup, toBeRecycled: View?): View {
        if (toBeRecycled == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflater.inflate(R.layout.ratings_row, parent, false)
        }
        return toBeRecycled
    }
}