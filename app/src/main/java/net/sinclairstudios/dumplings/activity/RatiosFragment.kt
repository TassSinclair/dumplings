package net.sinclairstudios.dumplings.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.calculation.DumplingServingAccumulator
import net.sinclairstudios.dumplings.domain.DumplingOrder
import net.sinclairstudios.dumplings.domain.DumplingOrderListFactory
import net.sinclairstudios.dumplings.domain.DumplingRating
import net.sinclairstudios.dumplings.domain.DumplingsRatings
import net.sinclairstudios.dumplings.util.TextViewUpdater

class RatiosFragment : Fragment(R.layout.ratios_main_layout) {

    private lateinit var ratiosChooseRatingsButton: Button
    private lateinit var howManyServingsSeekBar: SeekBar
    private lateinit var preferEvenServingsSwitch: SwitchMaterial
    private lateinit var howManyServingsTextView: TextView
    private lateinit var calculateRatiosButton: Button

    private val dumplingsRatings: DumplingsRatings = DumplingsRatings()
    private val orderListFactory: DumplingOrderListFactory = DumplingOrderListFactory()

    private val dumplingRatingsActivityResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                dumplingsRatings.set(
                    result.data!!.getSerializableExtra(DumplingsRatings::class.simpleName) as Array<DumplingRating>
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getPreferences()
        if (preferences.contains(DumplingRating::class.simpleName)) {
            dumplingsRatings.populate(preferences)
        } else {
            dumplingsRatings.populate(resources)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        super.onCreateView(inflater, container, savedInstanceState)?.also {
            initChooseRatingsButton(it.findViewById(R.id.ratiosChooseRatingsButton))
            howManyServingsTextView = it.findViewById(R.id.ratiosHowManyServingsTextView)
            initHowManyServingsSeekBar(it.findViewById(R.id.ratiosHowManyServingsSeekBar), howManyServingsTextView)
            initCalculateRatiosButton(it.findViewById(R.id.calculateRatiosButton))
            initEvenServingsSwitch(it.findViewById(R.id.preferEvenServingsSwitch))
        }

    private fun initEvenServingsSwitch(switch: SwitchMaterial) {
        preferEvenServingsSwitch = switch
        val preferMultiplesOfTwo = getPreferences().getBoolean(Switch::class.java.name, false)
        preferEvenServingsSwitch.isChecked = preferMultiplesOfTwo

    }

    private fun initChooseRatingsButton(button: Button) {
        ratiosChooseRatingsButton = button
        ratiosChooseRatingsButton.setOnClickListener {
            val intent = Intent(activity, RatingsActivity::class.java)
            intent.putExtra(DumplingsRatings::class.simpleName, dumplingsRatings.get())
            dumplingRatingsActivityResult.launch(intent)
        }
    }

    private fun initCalculateRatiosButton(button: Button) {
        calculateRatiosButton = button
        calculateRatiosButton.setOnClickListener {
            val intent = Intent(
                activity,
                YourOrderActivity::class.java
            )
            val requiredServings: Int = howManyServingsSeekBar.progress
            val preferMultiplesOf = if (preferEvenServingsSwitch.isChecked) 2 else 1

            val accumulator = DumplingServingAccumulator()
            accumulator.add(
                orderListFactory.createFromDumplingRatings(
                    dumplingsRatings.get().asList(),
                    requiredServings,
                    preferMultiplesOf
                )
            )

            intent.putExtra(
                DumplingOrder::class.simpleName,
                accumulator.getOrders().toTypedArray()
            )
            startActivity(intent)
        }
    }

    private fun initHowManyServingsSeekBar(seekBar: SeekBar, textView: TextView) {
        howManyServingsSeekBar = seekBar

        val changeListener = object : SeekBar.OnSeekBarChangeListener {

            val textViewUpdater = TextViewUpdater(textView)

            override fun onProgressChanged(seekBar: SeekBar, value: Int, b: Boolean) {
                textViewUpdater.update(value)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        }

        val progress = getPreferences().getInt(
            SeekBar::class.java.name,
            resources.getInteger(R.integer.defaultServings)
        )
        changeListener.onProgressChanged(seekBar, progress, false)
        howManyServingsSeekBar.setOnSeekBarChangeListener(changeListener)
        howManyServingsSeekBar.setProgress(progress, false)
    }

    override fun onPause() {
        super.onPause()

        val editor = getPreferences().edit()
        dumplingsRatings.depopulate(editor)

        editor
            .putInt(SeekBar::class.java.name, howManyServingsSeekBar.progress)
            .putBoolean(Switch::class.java.name, preferEvenServingsSwitch.isChecked)
            .apply()
    }

    private fun getPreferences(): SharedPreferences {
        return requireActivity().getPreferences(Activity.MODE_PRIVATE)
    }
}