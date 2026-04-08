package com.m1x.gymmer.ui.screens.viewmodel

import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.network.models.*
import com.m1x.gymmer.data.repository.GymmerRepository
import com.m1x.gymmer.data.utils.LogManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BusinessInsightsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: GymmerApplication
    private lateinit var repository: GymmerRepository
    private lateinit var logManager: LogManager
    private lateinit var viewModel: BusinessInsightsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        application = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        logManager = mockk(relaxed = true)
        
        mockkObject(LogManager.Companion)
        every { LogManager.getInstance(any(), any()) } returns logManager
        
        every { application.repository } returns repository
        every { application.logManager } returns logManager

        // Mock responses
        coEvery { repository.getRevenueKinetics() } returns listOf(
            RevenueDataPoint("Jan", 1000.0),
            RevenueDataPoint("Feb", 1200.0)
        )
        coEvery { repository.getPulse() } returns listOf(
            GymPulse(gymName = "Gym A", currentCapacity = 75)
        )
        coEvery { repository.getBusinessInsights() } returns BusinessInsights(
            annualRevenue = 50000.0,
            retentionRate = 85.0,
            memberGrowth = 200
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData updates uiState with repository data`() = runTest {
        viewModel = BusinessInsightsViewModel(application)
        
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals("₹50000.0", state.annualRevenue.amount)
        assertEquals("85.0%", state.retention.percentage)
        assertEquals("200", state.growth.activeTotal)
        assertEquals(2, state.revenueKinetics.chartData.size)
        assertEquals(1, state.gymPulse.size)
        assertEquals("Gym A", state.gymPulse[0].name)
    }

    @Test
    fun `onPeriodSelected updates selectedPeriod`() = runTest {
        viewModel = BusinessInsightsViewModel(application)
        advanceUntilIdle()
        
        viewModel.onPeriodSelected("Monthly")
        
        assertEquals("Monthly", viewModel.uiState.value.revenueKinetics.selectedPeriod)
    }
}
