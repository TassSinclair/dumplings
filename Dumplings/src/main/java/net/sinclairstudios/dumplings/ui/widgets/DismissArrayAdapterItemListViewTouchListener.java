package net.sinclairstudios.dumplings.ui.widgets;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.android.swipedismiss.SwipeDismissListViewTouchListener;

public class DismissArrayAdapterItemListViewTouchListener extends SwipeDismissListViewTouchListener {

    public DismissArrayAdapterItemListViewTouchListener(ListView listView, ArrayAdapter<?> arrayAdapter) {
        super(listView, dismissCallbacksForArrayAdapter(arrayAdapter));
    }

    private static <T> DismissCallbacks dismissCallbacksForArrayAdapter(final ArrayAdapter<T> arrayAdapter) {
        return new DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return position < arrayAdapter.getCount();
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                }
                arrayAdapter.notifyDataSetChanged();
            }
        };
    }
}
