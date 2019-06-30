package fixedTokenExample.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.types.IssuedTokenType
import com.r3.corda.lib.tokens.workflows.flows.shell.IssueTokens
import fixedTokenExample.types.MarketTokenType
import net.corda.core.contracts.Amount
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import java.math.BigDecimal

@StartableByRPC
class IssueMarketTokenFlow(val currency: String, val quantity: Long) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    // FiatCurrency is a token representing off-ledger value
    @Suspendable
    override fun call(): SignedTransaction {
        //val token = FiatCurrency.getInstance(currency)
        //val token = MarketTokenType.getInstance("WIBBLE")

        //val issuedToken = IssuedTokenType(ourIdentity, MarketTokenType.getInstance("PXL"))
        //val token = IssuedTokenType(ourIdentity, MarketTokenType.getInstance("PXL"))

        // Fungible tokens held by issuer
        val fungtoken = FungibleToken(Amount(quantity, BigDecimal(1), IssuedTokenType(ourIdentity, MarketTokenType.getInstance(currency))), ourIdentity)
        //val token: FungibleToken<MarketTokenType> =  (FungibleToken)MarketTokenType.getInstance("MME")

        // Starts a new flow session.
        //return subFlow(IssueTokens(amount of issuedToken issuedBy ourIdentity heldBy recipient))
        return subFlow(IssueTokens(fungtoken))
    }
}
