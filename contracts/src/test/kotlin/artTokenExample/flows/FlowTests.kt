package artTokenExample.flows

import artTokenExample.states.ArtWorkTokenType
import com.r3.corda.lib.tokens.money.FiatCurrency
import net.corda.core.contracts.Amount

import net.corda.core.node.services.queryBy
import net.corda.testing.common.internal.testNetworkParameters
import net.corda.testing.core.DUMMY_NOTARY_NAME
import net.corda.testing.node.*
import net.corda.testing.node.TestCordapp.Companion.findCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class FlowTests {
   // private val netParam = MockNetworkParameters(false, networkParameters = testNetworkParameters(minimumPlatformVersion = 4))
    private val netParam2 = MockNetworkParameters(
           networkSendManuallyPumped = false,
           threadPerNode = false,
           servicePeerAllocationStrategy = InMemoryMessagingNetwork.ServicePeerAllocationStrategy.Random(),
           notarySpecs = listOf(MockNetworkNotarySpec(DUMMY_NOTARY_NAME,false)),
           networkParameters = testNetworkParameters(minimumPlatformVersion = 4),
           cordappsForAllNodes = listOf(findCordapp("artTokenExample"),findCordapp("com.r3.corda.lib.tokens.contracts"),
                   findCordapp("com.r3.corda.lib.tokens.workflows"))
   )
    private val efw = MockNetworkParameters()
  //  private val network = MockNetwork(MockNetworkParameters(listOf(findCordapp("artTokenExample"))))

    private val network = MockNetwork(netParam2)

    private lateinit var nodeA: StartedMockNode;
    private lateinit var nodeB: StartedMockNode;
    private lateinit var nodeC: StartedMockNode;
    private lateinit var amount: Amount<FiatCurrency>
    @Before
    fun setUp(){
        nodeA = network.createPartyNode()
        nodeB = network.createPartyNode()
        nodeC = network.createPartyNode()
        amount  = Amount(1000, FiatCurrency(Currency.getInstance("USD"))) //swap out for custom tokem
        network.runNetwork()
    }

    @Test
    fun artTokensCreationOneOutput(){
        val transactionFut = nodeA.startFlow(ArtWorkTokenCreate("IDK",amount,"PICASSO"))
        network.runNetwork()
        val signedTransaction = transactionFut.get()
        assertEquals(1, signedTransaction.tx.outputStates.size)

    }

    @Test
    fun artTokenCreationZeroInput(){
        var transactionFut = nodeA.startFlow(ArtWorkTokenCreate("IDK",amount,"PICASSO"))
        network.runNetwork()
        val signedTransaction = transactionFut.get()
        assertEquals(0, signedTransaction.tx.inputs.size)
    }

    @Test
    fun artTokenChooseValuation(){
        var transactionFut = nodeA.startFlow(ArtWorkTokenCreate("IDK",amount,"PICASSO"))
        network.runNetwork()
        val signedTransaction = transactionFut.get()
        val output = signedTransaction.tx.outputsOfType(ArtWorkTokenType::class.java).get(0)
        assertEquals(amount,output.valuation)
    }

    @Test
    fun artTokenIssuingOwnerShip(){
        var transactionFut = nodeA.startFlow(ArtWorkTokenCreate("IDK",amount,"PICASSO"))
        network.runNetwork()
        val signedTransaction = transactionFut.get()
        val state = nodeA.services.vaultService.queryBy<ArtWorkTokenType>().states.get(0)
        val artWorkState = state.state.data
        var issuenceFut = nodeA.startFlow(ArtWorkTokenIssue(artWorkState.linearId.toString(),10, nodeB.info.legalIdentities.get(0)))
        network.runNetwork()
        val issuedTx = issuenceFut.get()
        assert(issuedTx.tx.requiredSigningKeys.contains(nodeA.info.legalIdentities.get(0).owningKey))

    }


    @After
    fun cleanUp() {
        network.stopNodes()
    }
}