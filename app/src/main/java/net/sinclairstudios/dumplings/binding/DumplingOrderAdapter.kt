package net.sinclairstudios.dumplings.binding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingOrder
import net.sinclairstudios.dumplings.domain.DumplingOrderViewHook
import net.sinclairstudios.dumplings.util.CountTracker

class DumplingOrderAdapter(
    context: Context,
    dumplingOrders: List<DumplingOrder>,
    private val dumplingDefaults: DumplingDefaults,
    private val masterCountTracker: CountTracker,
) : ArrayAdapter<DumplingOrder>(context, 0, dumplingOrders) {

    override fun getView(position: Int, toBeRecycled: View?, parent: ViewGroup): View {
        val dumplingOrder = getItem(position)!!
        val row = createOrRecycleView(parent, toBeRecycled)
        val dumplingNameTextView: TextView = row.findViewById(R.id.dumplingNameTextView)
        val dumplingImageView: ImageView = row.findViewById(R.id.dumplingImage)

        val viewHook = DumplingOrderViewHook(dumplingOrder)

        val icon = dumplingDefaults.getIcon(dumplingOrder.servings.dumpling.name)
        dumplingImageView.setImageDrawable(icon)
        viewHook.bind(
            dumplingNameTextView,
            (row.findViewById<View>(R.id.dumplingCheckboxHolder) as TableLayout)!!,
            masterCountTracker, context
        )
        return row
    }

    private fun createOrRecycleView(parent: ViewGroup, toBeRecycled: View?): View {
        if (toBeRecycled == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return inflater.inflate(R.layout.your_order_row, parent, false)
        }
        return toBeRecycled
    }
}