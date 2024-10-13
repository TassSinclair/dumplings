package net.sinclairstudios.dumplings.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import net.sinclairstudios.dumplings.R
import net.sinclairstudios.dumplings.binding.DumplingBinderFactory
import net.sinclairstudios.dumplings.domain.HasDumpling
import net.sinclairstudios.dumplings.widget.ListenerTrackingEditTextView

class DumplingNameDialogFragment(val dumplingBinderFactory: DumplingBinderFactory, val hasDumpling: HasDumpling) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        val dialogView: View = inflater.inflate(R.layout.dumpling_name_layout, null)

        builder
            .setView(dialogView)
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, which -> this@DumplingNameDialogFragment.dialog!!.dismiss() }


        val editText: ListenerTrackingEditTextView = dialogView.findViewById(R.id.dumplingNameEditText)
        val imageView = dialogView.findViewById<View>(R.id.dumplingImage) as ImageView

        dumplingBinderFactory.bindListenerTrackingEditText(hasDumpling, editText)
        dumplingBinderFactory.bindImageView(editText, imageView)
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        requireDialog().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    class Factory(val parent: FragmentManager,
                        val dumplingBinderFactory: DumplingBinderFactory,
                        val hasDumpling: HasDumpling) : View.OnClickListener {

        private var dialogFragment: DumplingNameDialogFragment? = null

        override fun onClick(view: View?) {
            if (dialogFragment == null) {
                dialogFragment = DumplingNameDialogFragment(dumplingBinderFactory, hasDumpling).also {
                    it.show(parent, "DumplingNameDialogFragment-$hasDumpling")
                }
            }
        }
    }
}