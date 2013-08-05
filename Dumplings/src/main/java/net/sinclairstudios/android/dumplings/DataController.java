package net.sinclairstudios.android.dumplings;

public interface DataController<T> {
    void reset();
    T get();
    void set(T it);

}
