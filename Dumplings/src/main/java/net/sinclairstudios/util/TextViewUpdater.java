package net.sinclairstudios.util;

import android.widget.TextView;

import java.text.DecimalFormat;

public class TextViewUpdater {
    private final TextView textView;
    private final String fieldValue;

    public TextViewUpdater(TextView textView, String fieldValue) {
        this.textView = textView;
        this.fieldValue = fieldValue;
    }

    public void update(int value) {
        textView.setText(fieldValue.replace("{}", DecimalFormat.getInstance().format(value)));
    }
}
