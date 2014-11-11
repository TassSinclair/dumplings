package net.sinclairstudios.dumplings.domain
import java.io.Serializable

public class DumplingServings(override var dumpling : Dumpling, var servings : Int) : Serializable, HasDumpling {

    public override fun toString() : String {
        return "${dumpling.toString()} ($servings servings)"
    }
}
