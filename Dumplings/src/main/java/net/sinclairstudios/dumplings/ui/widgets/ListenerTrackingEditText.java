package net.sinclairstudios.dumplings.ui.widgets;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;

public class ListenerTrackingEditText extends AutoCompleteTextView {

    private ArrayList<TextWatcher> textChangedListeners;

    public ListenerTrackingEditText(Context context) {
        super(context);
        textChangedListeners.clear();
    }

    public ListenerTrackingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        textChangedListeners.clear();
    }

    public ListenerTrackingEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textChangedListeners.clear();
    }


    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        ensureTextChangedListenerListIsSetUp();
        textChangedListeners.add(watcher);
        super.addTextChangedListener(watcher);
    }

    public void clearTextChangedListeners() {
        ensureTextChangedListenerListIsSetUp();
        for (TextWatcher textChangedListener : textChangedListeners) {
            super.removeTextChangedListener(textChangedListener);
        }
        textChangedListeners.clear();
    }

    private void ensureTextChangedListenerListIsSetUp() {
        if (textChangedListeners == null) {
            textChangedListeners = new ArrayList<TextWatcher>();
        }
    }
}
