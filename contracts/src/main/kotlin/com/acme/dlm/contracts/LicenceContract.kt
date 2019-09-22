package com.acme.dlm.contracts

import net.corda.core.contracts.*
import net.corda.core.identity.CordaX500Name
import net.corda.core.transactions.LedgerTransaction

class LicenceContract : Contract {
    override fun verify(tx: LedgerTransaction) {
    }
}
