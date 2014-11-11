package net.sinclairstudios.dumplings.ui.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import net.sinclairstudios.dumplings.domain.Dumpling;
import net.sinclairstudios.dumplings.domain.DumplingRating;
import net.sinclairstudios.dumplings.domain.HasDumpling;
import org.jetbrains.annotations.NotNull;

public class DumplingNameBinderTextWatcher implements TextWatcher {

    private final HasDumpling hasDumpling;

    public DumplingNameBinderTextWatcher(HasDumpling hasDumpling) {
        this.hasDumpling = hasDumpling;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(@NotNull CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        hasDumpling.setDumpling(new Dumpling(s.toString()));
        Log.d(DumplingNameBinderTextWatcher.class.getName(), "Change name event received: " + hasDumpling);
    }
}
