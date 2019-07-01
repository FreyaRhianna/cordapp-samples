package fixedTokenExample

import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import fixedTokenExample.flows.IssueMarketTokenFlow
import fixedTokenExample.flows.Responder
import fixedTokenExample.types.MarketTokenType
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

class FlowTests {
//    private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
//            TestCordapp.findCordapp("fixedTokenExample.contracts"),
//            TestCordapp.findCordapp("fixedTokenExample.flows"),
//            TestCordapp.findCordapp("com.r3.corda.lib.tokens")
//    )))
//    private val a = network.createNode()
//    private val b = network.createNode()

    init {
//        listOf(a, b).forEach {
//            it.registerInitiatedFlow(Responder::class.java)
//        }
    }

    private lateinit var network: MockNetwork
//    private var a: StartedMockNode? = null
//    private var b: StartedMockNode? = null
    private lateinit var a: StartedMockNode
    private lateinit var b: StartedMockNode
    private lateinit var marketTokenCurrency: String // type of marketToken
    private var quantity: Long = 0

    @Before
    fun setup() {
        network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
                TestCordapp.findCordapp("fixedTokenExample.contracts"),
                TestCordapp.findCordapp("fixedTokenExample.flows"),
                TestCordapp.findCordapp("com.r3.corda.lib.tokens.contracts")
        )))
        a = network.createNode()
        b = network.createNode()
        listOf(a, b).forEach {
            it.registerInitiatedFlow(Responder::class.java)
        }

        marketTokenCurrency = "PXL"
        quantity = 1000

        println("I HAVE SET a: $a")
        println("I HAVE ALSO SET b: $b")
    }

    @After
    fun tearDown() = network.stopNodes()

    // transaction should have one output
    @Test
    fun checkIssueMarketTokenOneOutput() {
        val transaction = a.startFlow(IssueMarketTokenFlow(currency = marketTokenCurrency, quantity = quantity))
        val signedTransaction = transaction.get()
        assertEquals(1, signedTransaction.tx.outputStates.size)
    }

    @Test
    fun checkIssueMarketTokenZeroInput() {
        val transaction = a.startFlow(IssueMarketTokenFlow(currency = marketTokenCurrency, quantity = quantity))
        val signedTransaction = transaction.get()
        assertEquals(0, signedTransaction.tx.inputs.size)
    }

    @Test
    fun checkIssueMarketTokenQuantity() {
        val transaction = a.startFlow(IssueMarketTokenFlow(currency = marketTokenCurrency, quantity = quantity))
        val signedTransaction = transaction.get()
        val output = signedTransaction.tx.outputsOfType(FungibleToken::class.java)[0]
        assertEquals(quantity, output.amount.quantity)
    }
}