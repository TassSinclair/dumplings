package net.sinclairstudios.android.dumplings.domain
import java.io.Serializable

public open class DumplingOrder(val dumpling : Dumpling, val servings : Int) : Serializable {
    public override fun toString() : String {
        return dumpling.toString() + " (" + servings + " servings)"
    }
}
