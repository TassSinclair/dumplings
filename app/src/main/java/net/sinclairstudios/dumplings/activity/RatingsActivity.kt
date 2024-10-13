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
import androidx.fragment.app.FragmentActivity
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.binding.DumplingRatingAdapter
import net.sinclairstudios.dumplings.domain.Dumpling
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingRating
import net.sinclairstudios.dumplings.domain.DumplingsRatings
import net.sinclairstudios.dumplings.domain.Rating
import net.sinclairstudios.dumplings.widget.DismissArrayAdapterItemListViewTouchListener

class RatingsActivity : AppCompatActivity(R.layout.list_layout) {

    private lateinit var listView: ListView

    private lateinit var dumplingRatings: MutableList<DumplingRating>

    private lateinit var dumplingRatingAdapter: DumplingRatingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView = findViewById(android.R.id.list)
        dumplingRatings =
            (intent.getSerializableExtra(DumplingsRatings::class.simpleName) as Array<DumplingRating>).toMutableList()
        dumplingRatingAdapter = DumplingRatingAdapter(DumplingDefaults(this), this, dumplingRatings)

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
        listView.adapter = dumplingRatingAdapter

        val touchListener = DismissArrayAdapterItemListViewTouchListener(
            listView, dumplingRatingAdapter
        )

        listView.setOnTouchListener(touchListener)

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener())
    }

    fun addDumpling(button: View?) {
        dumplingRatings.add(DumplingRating(Dumpling(""), Rating(0)))
        dumplingRatingAdapter.notifyDataSetChanged()
    }

    override fun finish() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(DumplingsRatings::class.simpleName,
            dumplingRatings.toTypedArray())
        setResult(RESULT_OK, intent)
        super.finish()
    }
}