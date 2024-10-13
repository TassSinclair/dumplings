package net.sinclairstudios.dumplings.domain

interface DataControl<T> {
    fun reset()
    fun get(): T
    fun set(it: T)
}