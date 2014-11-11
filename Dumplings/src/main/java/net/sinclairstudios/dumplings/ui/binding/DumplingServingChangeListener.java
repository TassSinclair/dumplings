package net.sinclairstudios.dumplings.ui.binding;

import android.widget.SeekBar;
import net.sinclairstudios.dumplings.domain.DumplingServings;
import net.sinclairstudios.util.TextViewUpdater;
import org.jetbrains.annotations.NotNull;

public class DumplingServingChangeListener implements SeekBar.OnSeekBarChangeListener {

    private final DumplingServings dumplingServings;
    private final TextViewUpdater textViewUpdater;

    public DumplingServingChangeListener(DumplingServings dumplingServings, TextViewUpdater textViewUpdater) {
        this.dumplingServings = dumplingServings;
        this.textViewUpdater = textViewUpdater;
    }

    @Override
    public void onProgressChanged(@NotNull SeekBar seekBar, int progress, boolean fromUser) {
        dumplingServings.setServings(progress);
        textViewUpdater.update(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(@NotNull SeekBar seekBar) {
    }
}