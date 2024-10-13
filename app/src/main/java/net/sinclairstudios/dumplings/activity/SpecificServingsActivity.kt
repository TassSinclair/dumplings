package net.sinclairstudios.dumplings.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.binding.DumplingServingAdapter
import net.sinclairstudios.dumplings.domain.Dumpling
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingServings
import net.sinclairstudios.dumplings.domain.DumplingsServings
import net.sinclairstudios.dumplings.widget.DismissArrayAdapterItemListViewTouchListener

class SpecificServingsActivity : AppCompatActivity(R.layout.list_layout){
    private lateinit var listView: ListView

    private lateinit var dumplingServings: MutableList<DumplingServings>

    private lateinit var dumplingServingAdapter: DumplingServingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView = findViewById(android.R.id.list)
        dumplingServings =
            (intent.getSerializableExtra(DumplingsServings::class.simpleName) as Array<DumplingServings>).toMutableList()
        dumplingServingAdapter = DumplingServingAdapter(DumplingDefaults(this), this, dumplingServings)

        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                        return true
                    }
                }
                return false
            }
        })


        val settingView = layoutInflater.inflate(R.layout.list_footer, null)
        listView.addFooterView(settingView)
        listView.adapter = dumplingServingAdapter

        val touchListener = DismissArrayAdapterItemListViewTouchListener(
            listView, dumplingServingAdapter
        )

        listView.setOnTouchListener(touchListener)

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener())
    }

    fun addDumpling(button: View?) {
        dumplingServings.add(DumplingServings(Dumpling(""), 0))
        dumplingServingAdapter.notifyDataSetChanged()
    }

    override fun finish() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(DumplingsServings::class.simpleName,
            dumplingServings.toTypedArray())
        setResult(RESULT_OK, intent)
        super.finish()
    }
}