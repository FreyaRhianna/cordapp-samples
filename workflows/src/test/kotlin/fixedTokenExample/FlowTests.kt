package fixedTokenExample

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

    @Before
    fun setup() {
        network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
                TestCordapp.findCordapp("fixedTokenExample.contracts"),
                TestCordapp.findCordapp("fixedTokenExample.flows"),
                TestCordapp.findCordapp("com.r3.corda.lib.tokens.contracts")
        )))
//        MockNetwork(listOf("fixedTokenExample.contracts", "fixedTokenExample.flows"))
        a = network.createNode()
        b = network.createNode()
        listOf(a, b).forEach {
            it.registerInitiatedFlow(Responder::class.java)
        }
//        network.runNetwork()
        println("I HAVE SET a: $a")
        println("I HAVE ALSO SET b: $b")
    }

//    @After
//    fun tearDown() = network.stopNodes()

    // transaction should have one output
    @Test
    fun checkIssueMarketTokenOneOutput() {
        val transaction = a!!.startFlow(IssueMarketTokenFlow(currency = "PXL", quantity = 1000))
        val signedTransaction = transaction.get()
        assertEquals(1, signedTransaction.tx.outputStates.size)
    }

    @Test
    fun checkIssueMarketTokenZeroInput() {
        val transaction = a!!.startFlow(IssueMarketTokenFlow(currency = "PXL", quantity = 1000))
        val signedTransaction = transaction.get()
        assertEquals(0, signedTransaction.tx.inputs.size)
    }
}