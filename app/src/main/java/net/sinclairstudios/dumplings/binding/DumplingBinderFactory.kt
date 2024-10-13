package net.sinclairstudios.dumplings.binding

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.domain.Dumpling
import net.sinclairstudios.dumplings.domain.DumplingDefaults
import net.sinclairstudios.dumplings.domain.DumplingRating
import net.sinclairstudios.dumplings.domain.DumplingServings
import net.sinclairstudios.dumplings.domain.HasDumpling
import net.sinclairstudios.dumplings.domain.Rating
import net.sinclairstudios.dumplings.util.TextViewUpdater
import net.sinclairstudios.dumplings.widget.ListenerTrackingEditTextView

class DumplingBinderFactory(val context: Context, val dumplingDefaults: DumplingDefaults) {
    fun bindRatingBar(dumplingRating: DumplingRating, ratingRatingBar: RatingBar) {
        ratingRatingBar.onRatingBarChangeListener = null
        ratingRatingBar.progress = dumplingRating.rating.value
        ratingRatingBar.setOnRatingBarChangeListener {
            ratingBar: RatingBar, rating: Float, fromUser: Boolean ->
            dumplingRating.rating = Rating(ratingBar.progress)
            Log.d(
                DumplingBinderFactory::class.simpleName,
                "Change rating event received: $dumplingRating"
            )
        }
    }

    fun bindListenerTrackingEditText(hasDumpling: HasDumpling, editText: ListenerTrackingEditTextView) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                hasDumpling.dumpling = Dumpling(editable.toString())
            }

        })
        editText.setAdapter(
            ArrayAdapter(context, R.layout.drop_down_dumpling, dumplingDefaults.defaultNames)
        )
        editText.append(hasDumpling.dumpling.name)
    }

    fun bindImageView(textView: TextView, imageView: ImageView) {
        textView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                imageView.setImageDrawable(dumplingDefaults.getIcon(charSequence.toString()))
            }
            override fun afterTextChanged(p0: Editable?) {
            }

        })
        imageView.setImageDrawable(dumplingDefaults.getIcon(textView.text.toString()))
    }

    fun bindSeekBar(
        dumplingServings: DumplingServings,
        textViewUpdater: TextViewUpdater,
        seekBar: SeekBar
    ) {
        seekBar.setOnSeekBarChangeListener(null)
        seekBar.progress = dumplingServings.servings
        val dumplingServingChangeListener = object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, p2: Boolean) {
                dumplingServings.servings = progress
                textViewUpdater.update(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        }
        seekBar.setOnSeekBarChangeListener(dumplingServingChangeListener)
        dumplingServingChangeListener.onProgressChanged(seekBar, seekBar.progress, false)
    }
}