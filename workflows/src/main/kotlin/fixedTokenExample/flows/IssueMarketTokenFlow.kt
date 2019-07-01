package fixedTokenExample.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.workflows.flows.shell.IssueTokens
import fixedTokenExample.types.MarketTokenType
import net.corda.core.contracts.Amount
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import java.math.BigDecimal

@InitiatingFlow
@StartableByRPC
class IssueMarketTokenFlow(val currency: String, val quantity: Long) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call(): SignedTransaction {
        // Fungible tokens held by issuer
        val fungMarkettoken = FungibleToken(Amount(quantity, BigDecimal(1), IssuedTokenType(ourIdentity, MarketTokenType.getInstance(currency))), ourIdentity)

        // Starts a new flow session.
        return subFlow(IssueTokens(fungMarkettoken))
    }
}
