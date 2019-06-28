package com.template.types

import com.r3.corda.lib.tokens.contracts.types.TokenType

data class MarketTokenType(override val fractionDigits: Int, override val tokenIdentifier: String) : TokenType {
    override val tokenClass: Class<*> get() = javaClass
    override fun toString(): String = tokenIdentifier

    // Notes: companion objects are similar to static methods (code not related to an instantiation
    // registry contains <code>: MarketTokenType(), pairs where the instantiated
    // token conforms to the correct fractionDigits.
    companion object {
        private val registry = mapOf(
                Pair("WIBBLE", MarketTokenType(2, "MarketToken")),
                Pair("WOBBLE", MarketTokenType(5, "MarketToken"))
        )

        fun getInstance(code: String): MarketTokenType {
            return registry[code] ?: throw IllegalArgumentException("$code doesn't exist.")
        }
    }
}