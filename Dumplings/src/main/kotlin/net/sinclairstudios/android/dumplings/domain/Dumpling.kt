package net.sinclairstudios.android.dumplings.domain
import java.io.Serializable

public open class Dumpling(val name: String): Serializable {

    public override fun toString(): String {
        return "$name dumpling"
    }

    public override fun equals(that: Any?): Boolean {
        return that is Dumpling
            && equals(that)
    }

    public fun equals(that: Dumpling): Boolean {
        return this.name.equals(that.name)
    }
}
