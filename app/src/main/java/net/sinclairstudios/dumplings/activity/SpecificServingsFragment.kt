package net.sinclairstudios.dumplings.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.calculation.DumplingServingAccumulator
import net.sinclairstudios.dumplings.domain.DumplingOrder
import net.sinclairstudios.dumplings.domain.DumplingRating
import net.sinclairstudios.dumplings.domain.DumplingServings
import net.sinclairstudios.dumplings.domain.DumplingsServings
import net.sinclairstudios.dumplings.util.TextViewUpdater

class SpecificServingsFragment : Fragment(R.layout.specific_servings_main_layout) {
    private lateinit var specificServingsButton: Button
    private lateinit var howManyServingsTextView: TextView
    private lateinit var calculateServingsButton: Button

    private lateinit var howManyServingsTextViewUpdater: TextViewUpdater

    private val dumplingsServings = DumplingsServings()

    private val dumplingServingsActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                val servings = result.data!!.getSerializableExtra(
                    DumplingsServings::class.simpleName
                ) as Array<DumplingServings>

                dumplingsServings.set(servings)
                howManyServingsTextViewUpdater.update(servings.map { it.servings }.sum())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getPreferences()
        if (preferences.contains(DumplingRating::class.simpleName)) {
            dumplingsServings.populate(preferences)
        } else {
            dumplingsServings.populate(resources)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        super.onCreateView(inflater, container, savedInstanceState)?.also {
            howManyServingsTextView = it.findViewById(R.id.servingsHowManyServingsTextView)
            howManyServingsTextViewUpdater = TextViewUpdater(howManyServingsTextView)
            howManyServingsTextViewUpdater.update(dumplingsServings.get().map { it.servings }.sum())

            calculateServingsButton = it.findViewById(R.id.calculateServingsButton)
            calculateServingsButton.setOnClickListener {
                val intent = Intent(
                    activity,
                    YourOrderActivity::class.java
                )

                val accumulator = DumplingServingAccumulator()
                accumulator.add(dumplingsServings.get().asList())

                intent.putExtra(
                    DumplingOrder::class.simpleName,
                    accumulator.getOrders().toTypedArray()
                )
                startActivity(intent)
            }

            specificServingsButton = it.findViewById(R.id.specificServingsButton)
            specificServingsButton.setOnClickListener {
                val intent = Intent(activity, SpecificServingsActivity::class.java)
                intent.putExtra(DumplingsServings::class.simpleName, dumplingsServings.get())
                dumplingServingsActivityResult.launch(intent)
            }
        }

    override fun onPause() {
        super.onPause()
        val editor = getPreferences().edit()
        dumplingsServings.depopulate(editor)

        editor.apply()
    }

    private fun getPreferences(): SharedPreferences {
        return requireActivity().getPreferences(Activity.MODE_PRIVATE)
    }
}