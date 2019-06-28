package com.template.types

import com.r3.corda.lib.tokens.money.Money

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
    companion object {
        private val registry = mapOf(
                Pair("PXL", MarketTokenType("PXL", "Pixely", 6)),
                Pair("MUS", MarketTokenType("MUS", "Musicx", 6)),
                Pair("MME", MarketTokenType("MME", "MultMee", 6))
        )

        fun getInstance(code: String): MarketTokenType {
            return registry[code] ?: throw IllegalArgumentException("$code doesn't exist.")
        }
    }
}