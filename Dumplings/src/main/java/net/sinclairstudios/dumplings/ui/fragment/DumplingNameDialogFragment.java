package net.sinclairstudios.dumplings.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import net.sinclairstudios.dumplings.R;
import net.sinclairstudios.dumplings.domain.HasDumpling;
import net.sinclairstudios.dumplings.ui.binding.DumplingBinderFactory;
import net.sinclairstudios.dumplings.ui.widgets.ListenerTrackingEditText;
import org.jetbrains.annotations.NotNull;

public class DumplingNameDialogFragment extends DialogFragment {

    private final DumplingBinderFactory dumplingBinderFactory;
    private final HasDumpling hasDumpling;

    public DumplingNameDialogFragment(DumplingBinderFactory dumplingBinderFactory,
                                      HasDumpling hasDumpling) {
        this.dumplingBinderFactory = dumplingBinderFactory;
        this.hasDumpling = hasDumpling;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dumpling_name_layout, null);

        builder
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NotNull DialogInterface dialog, int which) {
                        DumplingNameDialogFragment.this.getDialog().dismiss();
                    }
                });


        ListenerTrackingEditText editText =
                (ListenerTrackingEditText) dialogView.findViewById(R.id.dumplingNameEditText);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.dumplingImage);

        dumplingBinderFactory.bindListenerTrackingEditText(hasDumpling, editText);
        dumplingBinderFactory.bindImageView(editText, imageView);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static class Spawner implements View.OnFocusChangeListener {

        private final FragmentManager parent;
        private final DumplingBinderFactory dumplingBinderFactory;
        private final HasDumpling hasDumpling;

        private DumplingNameDialogFragment dialogFragment;

        public Spawner(FragmentManager parent, DumplingBinderFactory dumplingBinderFactory,
                       HasDumpling hasDumpling) {
            this.parent = parent;
            this.dumplingBinderFactory = dumplingBinderFactory;
            this.hasDumpling = hasDumpling;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (dialogFragment == null) {
                    dialogFragment = new DumplingNameDialogFragment(dumplingBinderFactory, hasDumpling);
                    dialogFragment.show(parent, "DumplingNameDialogFragment-" + hasDumpling.toString());
                }
            }
        }
    }
}
