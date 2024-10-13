package net.sinclairstudios.dumplings.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import net.sinclairstudios.dumplings.R

class MainActivity : AppCompatActivity(R.layout.main_layout) {

    companion object {
        const val TAB_CHOICE_KEY: String = "TAB_CHOICE"
        const val VERSION_KEY: String = "VERSION"
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private var tabChoice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        tabChoice = getPreferences().getInt(TAB_CHOICE_KEY, 0)
        with(MainActivityPagerAdapter(this, tabLayout, viewPager)) {
            viewPager.adapter = this
            this.tabLayoutMediator.attach()
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabChoice = position
            }
        })
        viewPager.setCurrentItem(tabChoice, false)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.aboutMenuItem -> {
                        startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onPause() {
        super.onPause()
        getPreferences().edit()
            .putInt(TAB_CHOICE_KEY, tabChoice)
            .apply()
    }

    private fun getPreferences(): SharedPreferences {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val versionName = resources.getString(R.string.versionName)

        if (versionName != sharedPreferences.getString(VERSION_KEY, null)) {
            sharedPreferences
                .edit()
                .clear()
                .putString(VERSION_KEY, versionName)
                .apply()
        }
        return sharedPreferences
    }

    inner class MainActivityPagerAdapter(fa: FragmentActivity, tabLayout: TabLayout, viewPager2: ViewPager2) : FragmentStateAdapter(fa) {

        private val fragments = listOf(
            fa.baseContext.getString(R.string.ratingsAndRatiosLabel) to ::RatiosFragment,
            fa.baseContext.getString(R.string.specificServingsLabel) to ::SpecificServingsFragment,
        )

        val tabLayoutMediator =
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = fragments[position].first
            }

        override fun getItemCount(): Int =
            fragments.size

        override fun createFragment(position: Int): Fragment =
            fragments[position].second.invoke()

    }
}