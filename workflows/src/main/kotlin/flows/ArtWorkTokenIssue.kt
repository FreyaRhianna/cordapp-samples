package artTokenExample.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.utilities.withNotary
import com.r3.corda.lib.tokens.money.FiatCurrency
import com.r3.corda.lib.tokens.workflows.flows.evolvable.CreateEvolvableToken
import com.r3.corda.lib.tokens.workflows.flows.shell.IssueTokens
import artTokenExample.states.ArtWorkTokenType
import fixedTokenExample.types.MarketTokenType
import net.corda.core.contracts.Amount
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.transactions.SignedTransaction
import java.util.*

@StartableByRPC
class ArtWorkTokenIssue(
        val evolvableTokenId: String,
        val amount: Long,
        val recipient: Party
) : FlowLogic<SignedTransaction>() {
    //override val progressTracker = ProgressTracker()
    @Suspendable
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities.first()
        val uuid = UUID.fromString(evolvableTokenId)
        val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
        val tokenStateAndRef = serviceHub.vaultService.queryBy<ArtWorkTokenType>(queryCriteria).states.single()
        val token = tokenStateAndRef.state.data.toPointer<ArtWorkTokenType>()
        // Starting this flow with a new flow session.
        //val issueTokensFlow = IssueTokens(amount of token issuedBy ourIdentity heldBy recipient)
        val issueTokensFlow = IssueTokens(token, amount, ourIdentity, recipient)
        //(amount, token, ourIdentity, recipient, listOf())

        return subFlow(issueTokensFlow)
    }
}

@StartableByRPC
class ArtWorkTokenCreate(
        val data: String,
        val valuation: Amount<MarketTokenType>,
        val artist: String
) : FlowLogic<SignedTransaction>() {
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities.first()
        val artTokenType = ArtWorkTokenType(data, valuation, artist, ourIdentity)
        return subFlow(CreateEvolvableToken(artTokenType withNotary notary))
    }

}