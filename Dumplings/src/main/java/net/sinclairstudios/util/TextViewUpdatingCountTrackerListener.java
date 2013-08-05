package net.sinclairstudios.util;

import android.widget.TextView;

public class TextViewUpdatingCountTrackerListener implements CountTrackerListener {
    private final TextViewUpdater textViewUpdater;

    public TextViewUpdatingCountTrackerListener(TextView textView, String fieldValue) {
        this.textViewUpdater = new TextViewUpdater(textView, fieldValue);
    }

    @Override
    public void onCountTrackerAdd(int addition, int value) {
        textViewUpdater.update(value);
    }
}
