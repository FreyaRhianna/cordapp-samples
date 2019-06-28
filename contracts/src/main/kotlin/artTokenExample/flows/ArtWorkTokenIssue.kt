package artTokenExample.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.r3.corda.lib.tokens.contracts.utilities.heldBy
import com.r3.corda.lib.tokens.contracts.utilities.issuedBy
import com.r3.corda.lib.tokens.contracts.utilities.of
import com.r3.corda.lib.tokens.contracts.utilities.withNotary
import com.r3.corda.lib.tokens.money.FiatCurrency
import com.r3.corda.lib.tokens.workflows.flows.evolvable.CreateEvolvableToken
import com.r3.corda.lib.tokens.workflows.flows.shell.IssueTokens
import artTokenExample.states.ArtWorkTokenType
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
    @Suspendable
    override fun call(): SignedTransaction {
        val uuid = UUID.fromString(evolvableTokenId)
        val queryCriteria = QueryCriteria.LinearStateQueryCriteria(uuid = listOf(uuid))
        val tokenStateAndRef = serviceHub.vaultService.queryBy<EvolvableTokenType>(queryCriteria).states.single()
        val token = tokenStateAndRef.state.data.toPointer<ArtWorkTokenType>()
        // Starting this flow with a new flow session.
        val issueTokensFlow = IssueTokens(amount of token issuedBy ourIdentity heldBy recipient)
        return subFlow(issueTokensFlow)
    }
}

@StartableByRPC
class ArtWorkTokenCreate(
        val data: String,
        val valuation: Amount<FiatCurrency>,
        val artist: String
) : FlowLogic<SignedTransaction>() {
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities.first()
        val artTokenType = ArtWorkTokenType(data, valuation, artist, ourIdentity)
        return subFlow(CreateEvolvableToken(artTokenType withNotary notary))
    }

}