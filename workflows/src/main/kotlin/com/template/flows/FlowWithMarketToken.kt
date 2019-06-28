package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.money.FiatCurrency
import com.r3.corda.lib.tokens.workflows.flows.shell.IssueTokens
import com.template.types.MarketTokenType
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker

@StartableByRPC
class FlowWithMarketToken(val currency: String, val amount: Long, val recipient: Party) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    // FiatCurrency is a token representing off-ledger value
    @Suspendable
    override fun call(): SignedTransaction {
        //val token = FiatCurrency.getInstance(currency)
        val token = MarketTokenType.getInstance("WIBBLE")
        // Starts a new flow session.
        return subFlow(IssueTokens(amount of token issuedBy ourIdentity heldBy recipient))
    }
}
