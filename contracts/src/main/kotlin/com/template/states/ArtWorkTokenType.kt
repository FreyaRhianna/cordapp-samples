package com.template.states

import com.r3.corda.lib.tokens.contracts.states.EvolvableTokenType
import com.template.contracts.ArtWorkTokenTypeContract
import net.corda.core.contracts.Amount
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import com.r3.corda.lib.tokens.money.FiatCurrency

@BelongsToContract(ArtWorkTokenTypeContract::class)
class ArtWorkTokenType (
        val quality: String,
        val valuation : Amount<FiatCurrency>, //eventually this will be our issued tokens?
        val artist:String,  //this will be not editable in future states
        val maintainer: Party,
        override val fractionDigits: Int = 0,
        override val linearId: UniqueIdentifier = UniqueIdentifier())
    : EvolvableTokenType() {
    companion object {
        val contractId = this::class.java.enclosingClass.canonicalName
    }
    override val maintainers: List<Party> get() = listOf(maintainer)
}