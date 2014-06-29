package net.sinclairstudios.dumplings.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class LineSeparatorLinearLayout extends LinearLayout {

    private int fID = 0;

    public LineSeparatorLinearLayout(Context context) {
        super(context);
    }

    public LineSeparatorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void addView(View child) {
        if(getChildCount() > 0) {
            super.addView(createDivider());
        }
        super.addView(child);
    }

    private View createDivider() {
        View view = new View(getContext());
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.setMargins(32, 0, 32, 0);
        view.setLayoutParams(layoutParams);
        return view;
    }

    public List<View> getChildren() {
        ArrayList<View> views = new ArrayList<View>();
        for (int i = 0; i < getChildCount(); i = i + 2) {
            views.add(getChildAt(i));
        }
        return views;
    }

    public int findUnusedId() {
        while (findViewById(++fID) != null);
        return fID;
    }
}
