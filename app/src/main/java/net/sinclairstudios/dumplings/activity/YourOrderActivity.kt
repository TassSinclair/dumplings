package net.sinclairstudios.dumplings.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.binding.DumplingOrderAdapter
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingOrder
import net.sinclairstudios.dumplings.util.CountTracker
import net.sinclairstudios.dumplings.util.CountTrackerListener
import net.sinclairstudios.dumplings.util.TextViewUpdater

class YourOrderActivity : AppCompatActivity(R.layout.your_order_layout){
    private lateinit var dumplingOrders: List<DumplingOrder>
    private lateinit var servingCountdownTextView: TextView
    private lateinit var yourOrderList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dumplingOrders =
            (intent.getSerializableExtra(DumplingOrder::class.simpleName) as Array<DumplingOrder>).toList()

        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }

        servingCountdownTextView = findViewById(R.id.servingCountdownTextView)
        yourOrderList = findViewById(R.id.yourOrderList)
        var totalDumplings = 0
        for (dumplingOrder in dumplingOrders) {
            totalDumplings += dumplingOrder.servings.servings
        }
        val masterCountTracker: CountTracker = CountTracker(totalDumplings)
        masterCountTracker.addOnAddListener(object : CountTrackerListener {
            val textViewUpdater = TextViewUpdater(servingCountdownTextView)
            override fun onCountTrackerAdd(addition: Int, value: Int) {
                textViewUpdater.update(value)
            }
        })

        val dumplingDefaults = DumplingDefaults(this)
        yourOrderList.adapter = DumplingOrderAdapter(
            this,
            dumplingOrders,
            dumplingDefaults,
            masterCountTracker
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(
                    this,
                    MainActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}