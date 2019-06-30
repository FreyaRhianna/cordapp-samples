package artTokenExample.contracts

import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test
class ContractTests {

    private val artDealer = TestIdentity((CordaX500Name("Dealer", "London", "GB")))
    private val artBuyer = TestIdentity((CordaX500Name("Buyer", "London", "GB")))
    private val ledgerServices = MockServices(
            artDealer,
            artBuyer
    )
    private val dummyNotary = ledgerServices.networkMapCache.notaryIdentities.first()
    @Test
    fun `dummy test`() {

    }
/*
    @Test
    fun artCreationRequiresZeroInputs(){
        ledgerServices.ledger{
            transaction{
             //   input()
            }
        }
    }

    */
}