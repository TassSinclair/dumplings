package net.sinclairstudios.android.dumplings;

import android.content.res.Resources;
import android.os.Bundle;

public interface SerialisableViaResources {
    void populate(Resources bundle);
    void depopulate(Resources bundle);
}
