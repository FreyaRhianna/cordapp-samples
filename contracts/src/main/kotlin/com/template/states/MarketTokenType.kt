package com.template.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.AbstractParty

// *********
// * State *
// *********
//@BelongsToContract(MarketTokenContract::class)
data class MarketTokenType(val data: String, override val participants: List<AbstractParty> = listOf()) : Fung
