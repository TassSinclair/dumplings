package net.sinclairstudios.dumplings;

public interface DataController<T> {
    void reset();
    T get();
    void set(T it);

}
