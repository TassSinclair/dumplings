package net.sinclairstudios.dumplings.domain
import java.io.Serializable

public open class Dumpling(val name: String): Serializable {

    public override fun toString(): String {
        return "$name dumpling"
    }

    public override fun equals(o: Any?): Boolean {
        return o is Dumpling
            && equals(o)
    }

    public fun equals(that: Dumpling): Boolean {
        return this.name.equals(that.name)
    }
}
