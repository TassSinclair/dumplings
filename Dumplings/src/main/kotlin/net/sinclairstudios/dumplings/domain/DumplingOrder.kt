package net.sinclairstudios.dumplings.domain
import java.io.Serializable

public class DumplingOrder(var servings : DumplingServings) : Serializable {

    var arrived: Int = 0

    public override fun toString() : String {
        return "${servings.toString()} ($arrived arrived)"
    }
}
