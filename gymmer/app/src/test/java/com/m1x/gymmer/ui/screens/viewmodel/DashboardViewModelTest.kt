package com.m1x.gymmer.ui.screens.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        viewModel = DashboardViewModel()
    }

    @Test
    fun `initial state has active sessions`() {
        val state = viewModel.uiState.value
        assertEquals(1, state.activeSessions.size)
        assertEquals("Precision Tricep Blast", state.activeSessions[0].name)
    }

    @Test
    fun `onCheckInClicked updates checkInRequired`() {
        viewModel.onCheckInClicked()
        assertFalse(viewModel.uiState.value.checkInRequired)
    }
}
