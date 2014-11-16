package net.sinclairstudios.util;

import android.widget.TextView;

import java.text.DecimalFormat;

public class TextViewUpdater {
    private final TextView textView;
    private final String fieldValue;

    public TextViewUpdater(TextView textView) {
        this.textView = textView;
        this.fieldValue = textView.getText().toString();
    }

    public void update(int value) {
        textView.setText(fieldValue.replace("{}", DecimalFormat.getInstance().format(value)));
    }
}
