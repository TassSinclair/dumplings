package net.sinclairstudios.dumplings.domain
import java.io.Serializable

public class DumplingServings(var dumpling : Dumpling, var servings : Int) : Serializable {

    public override fun toString() : String {
        return "${dumpling.toString()} ($servings servings)"
    }
}
