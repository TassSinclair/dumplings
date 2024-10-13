package net.sinclairstudios.dumplings.util

class CountTracker(private var count: Int) {
    private val listeners = mutableSetOf<CountTrackerListener>()

    fun add(add: Int) {
        count += add
        listeners.forEach {
            it.onCountTrackerAdd(add, count)
        }
    }

    fun addOnAddListener(listener: CountTrackerListener) {
        listeners.add(listener)
        listener.onCountTrackerAdd(0, count)
    }
}