package com.template.contracts

import com.r3.corda.lib.tokens.contracts.FungibleTokenContract
import com.template.types.MarketTokenType
import jdk.nashorn.internal.parser.TokenType
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

/**
 * This doesn't do anything over and above the [FungibleTokenContract].
 */
// Check optional overrides
class MarketTokenContract : FungibleTokenContract<MarketTokenType>(), Contract {
}