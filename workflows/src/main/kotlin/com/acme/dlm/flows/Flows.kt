package com.acme.dlm.flows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder


@InitiatingFlow
@StartableByRPC
class IssueLicenceInitiator() : FlowLogic<SecureHash>() {
    @Suspendable
    override fun call(): SecureHash {
        val sessions = listOf(initiateFlow(ourIdentity))

        val utx = TransactionBuilder(
            serviceHub.networkMapCache.notaryIdentities.first()
        )
        utx.verify(serviceHub)
        val itx = serviceHub.signInitialTransaction(utx, ourIdentity.owningKey)
        val stx = subFlow(CollectSignaturesFlow(itx, sessions))
        val ftx = subFlow(FinalityFlow(stx, sessions))

        return ftx.id
    }
}


@InitiatedBy(IssueLicenceInitiator::class)
class IssueLicenceResponder(private val otherSession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        subFlow(
            ReceiveFinalityFlow(otherSideSession = otherSession)
        )
    }
}
