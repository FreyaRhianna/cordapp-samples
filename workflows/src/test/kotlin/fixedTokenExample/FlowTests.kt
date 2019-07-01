package fixedTokenExample

import com.r3.corda.lib.tokens.contracts.states.FungibleToken
import fixedTokenExample.flows.IssueMarketTokenFlow
import fixedTokenExample.flows.Responder
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FlowTests {

    init {
    }

    private lateinit var network: MockNetwork
    private lateinit var a: StartedMockNode
    private lateinit var b: StartedMockNode
    private lateinit var marketTokenCurrency: String // type of marketToken "PXL", "MUS", "MXE"
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

    // issuance should have no inputs
    @Test
    fun checkIssueMarketTokenZeroInput() {
        val transaction = a.startFlow(IssueMarketTokenFlow(currency = marketTokenCurrency, quantity = quantity))
        val signedTransaction = transaction.get()
        assertEquals(0, signedTransaction.tx.inputs.size)
    }

    // check that token quantity is correct
    @Test
    fun checkIssueMarketTokenQuantity() {
        val transaction = a.startFlow(IssueMarketTokenFlow(currency = marketTokenCurrency, quantity = quantity))
        val signedTransaction = transaction.get()
        val output = signedTransaction.tx.outputsOfType(FungibleToken::class.java)[0]
        assertEquals(quantity, output.amount.quantity)
    }
}