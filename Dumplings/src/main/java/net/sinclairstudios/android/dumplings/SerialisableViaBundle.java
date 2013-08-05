package net.sinclairstudios.android.dumplings;

import android.os.Bundle;

public interface SerialisableViaBundle {
    void populate(Bundle bundle);
    void depopulate(Bundle bundle);
}
