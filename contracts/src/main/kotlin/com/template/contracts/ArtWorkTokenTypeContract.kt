package com.template.contracts


import com.r3.corda.lib.tokens.contracts.EvolvableTokenContract
import com.template.states.ArtWorkTokenType
import net.corda.core.contracts.Amount
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class ArtWorkTokenTypeContract : EvolvableTokenContract(), Contract {
    override fun additionalCreateChecks(tx: LedgerTransaction) {
        val newArtwork = tx.outputStates.single() as ArtWorkTokenType
        newArtwork.apply {
            require(valuation > Amount.zero(valuation.token)) { "Valuation greater than 0"}
        }
    }

    override fun additionalUpdateChecks(tx: LedgerTransaction) {
        return
    }

}