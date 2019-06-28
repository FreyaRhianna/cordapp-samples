package artTokenExample.flows

import artTokenExample.states.ArtWorkTokenType
import com.r3.corda.lib.tokens.money.FiatCurrency
import net.corda.core.contracts.Amount
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp.Companion.findCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class FlowTests {
    private val network = MockNetwork(MockNetworkParameters(listOf(findCordapp("artTokenExample"))))
    private lateinit var nodeA: StartedMockNode;
    private lateinit var nodeB: StartedMockNode;
    private lateinit var nodeC: StartedMockNode;
    private lateinit var amount: Amount<FiatCurrency>
    @Before
    fun setUp(){
        nodeA = network.createNode()
        nodeB = network.createNode()
        nodeC = network.createNode()
        amount  = Amount(1000, FiatCurrency(Currency.getInstance("USD"))) //swap out for custom token


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


    @After
    fun cleanUp() {
        network.stopNodes()
    }
}