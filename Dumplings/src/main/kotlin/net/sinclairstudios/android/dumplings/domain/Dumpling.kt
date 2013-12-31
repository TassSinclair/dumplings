package net.sinclairstudios.android.dumplings.domain
import java.io.Serializable

public class Dumpling(val name : String) : Serializable {
    public override fun toString() : String {
        return name + " dumpling"
    }
}
