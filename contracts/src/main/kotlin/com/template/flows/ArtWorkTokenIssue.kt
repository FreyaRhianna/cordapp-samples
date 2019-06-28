package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken
import com.r3.corda.lib.tokens.contracts.types.TokenPointer
import com.r3.corda.lib.tokens.contracts.utilities.withNotary
import com.r3.corda.lib.tokens.money.FiatCurrency
import com.r3.corda.lib.tokens.workflows.flows.evolvable.CreateEvolvableToken
import com.template.states.ArtWorkTokenType
import net.corda.core.contracts.Amount
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import java.util.*

@StartableByRPC
class ArtWorkTokenIssue(val data: String) : FlowLogic<SignedTransaction>() {

    @Suspendable
    override fun call(): SignedTransaction {
        val notary = serviceHub.networkMapCache.notaryIdentities.first()
        val amount  = Amount(1000, FiatCurrency(Currency.getInstance("USD"))) //swap out for custom token
        val artTokenType = ArtWorkTokenType(data, amount,"PICASSO", ourIdentity)
        subFlow(CreateEvolvableToken(artTokenType withNotary notary))

        //val artTokenPtr = artTokenType.toPointer<ArtWorkTokenType>()
        //val artToken: FungibleToken<TokenPointer<ArtWorkTokenType>> = artTokenPtr issued by issuer
        //val transactionState = TransactionState(evolvableTokenType, notary = notary)
        //return subFlow(CreateEvolvableToken(transactionState))
    }
}

@StartableByRPC
class ArtWorkTokenCreate(val data: String) : FlowLogic<Unit>() {
    override fun call() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}