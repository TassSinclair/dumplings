package net.sinclairstudios.dumplings.widget

import android.widget.ArrayAdapter
import android.widget.ListView
import swipedismiss.SwipeDismissListViewTouchListener

class DismissArrayAdapterItemListViewTouchListener(listView: ListView, arrayAdapter: ArrayAdapter<*>) : SwipeDismissListViewTouchListener(
    listView, dismissCallbacksForArrayAdapter(arrayAdapter)
) {

    companion object {
        private fun <T> dismissCallbacksForArrayAdapter(arrayAdapter: ArrayAdapter<T>): DismissCallbacks {
            return object : DismissCallbacks {
                override fun canDismiss(position: Int): Boolean {
                    return position < arrayAdapter.count
                }

                override fun onDismiss(listView: ListView?, reverseSortedPositions: IntArray) {
                    for (position in reverseSortedPositions) {
                        arrayAdapter.remove(arrayAdapter.getItem(position))
                    }
                    arrayAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}