package com.m1x.gymmer.ui.screens.viewmodel

import com.m1x.gymmer.ui.screens.state.WalletUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WalletViewModelTest {

    @Test
    fun `initial state should contain mock payment history and reminders`() = runTest {
        val viewModel = WalletViewModel()
        val state = viewModel.uiState.first()

        assertNotNull(state.paymentHistory)
        assertEquals(3, state.paymentHistory.size)
        assertEquals("Membership Renewal", state.paymentHistory[0].title)
        assertEquals(9999.00, state.paymentHistory[0].amount, 0.0)

        assertNotNull(state.remindersHistory)
        assertEquals(3, state.remindersHistory.size)
        assertEquals("Monthly Invoice ready", state.remindersHistory[0].title)
    }
}
