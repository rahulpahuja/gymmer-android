package com.m1x.gymmer.ui.screens.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScanViewModelTest {

    private lateinit var viewModel: ScanViewModel

    @Before
    fun setup() {
        viewModel = ScanViewModel()
    }

    @Test
    fun `toggleFlashlight updates isFlashlightOn`() {
        assertFalse(viewModel.uiState.value.isFlashlightOn)
        viewModel.toggleFlashlight()
        assertTrue(viewModel.uiState.value.isFlashlightOn)
        viewModel.toggleFlashlight()
        assertFalse(viewModel.uiState.value.isFlashlightOn)
    }
}
