package artTokenExample.flows

import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.StartedMockNode
import net.corda.testing.node.TestCordapp.Companion.findCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTests {
    private val network = MockNetwork(MockNetworkParameters(listOf(findCordapp("artTokenExample"))))
    private lateinit var nodeA: StartedMockNode;
    private lateinit var nodeB: StartedMockNode;
    private lateinit var nodeC: StartedMockNode;

    @Before
    fun setUp(){
        nodeA = network.createNode()
        nodeB = network.createNode()
        nodeC = network.createNode()
    }

    @Test
    fun checkArtTokensCanBeCreated(){
        val transactionFut = nodeA.startFlow(ArtWorkTokenCreate("IDK"))
        network.runNetwork()
        val signedTransaction = transactionFut.get()
    }

    @After
    fun cleanUp() {
        network.stopNodes()
    }
}