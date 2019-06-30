package fixedTokenExample.types

import com.r3.corda.lib.tokens.money.Money
import com.r3.corda.lib.tokens.contracts.types.TokenType

/*
Defining a Token for a digital art market - Similar to [DigitalCurrency]
 */
data class MarketTokenType(
        override val tokenIdentifier: String,
        override val description: String,
        override val fractionDigits: Int = 0
) : Money {
    override val tokenClass: Class<*> get() = javaClass
    override fun toString(): String = tokenIdentifier

    // Notes: companion objects are similar to static methods (code not related to an instantiation
    // registry contains <code>: MarketTokenType(), pairs where the instantiated
    // token conforms to the correct fractionDigits.
    // PXL - pixelated art
    // MUS - Music
    // MME - multimedia works
    companion object {
        private val registry = mapOf(
                Pair("PXL", MarketTokenType("PXL", "Pixely", 0)),
                Pair("MUS", MarketTokenType("MUS", "Musicx", 0)),
                Pair("MME", MarketTokenType("MME", "MultMee", 0))
        )

        fun getInstance(code: String): MarketTokenType {
            return registry[code] ?: throw IllegalArgumentException("$code doesn't exist.")
        }
    }
}