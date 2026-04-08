package com.m1x.gymmer.ui.screens.viewmodel

import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.network.models.Exercise
import com.m1x.gymmer.data.network.models.TrainerDashboardData
import com.m1x.gymmer.data.network.models.User
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
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class TrainerStudioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: GymmerApplication
    private lateinit var repository: GymmerRepository
    private lateinit var logManager: LogManager
    private lateinit var viewModel: TrainerStudioViewModel

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

        // Default mock responses
        coEvery { repository.listExercises() } returns listOf(
            Exercise(id = UUID.randomUUID(), name = "Bench Press")
        )
        coEvery { repository.getTrainerDashboard(any()) } returns TrainerDashboardData(
            trainer = User(name = "Test Trainer"),
            activeTraineeCount = 5
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadStudioData updates uiState with repository data`() = runTest {
        viewModel = TrainerStudioViewModel(application)
        
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(1, state.machineLibrary.size)
        assertEquals("Bench Press", state.machineLibrary[0].name)
        assertEquals(1, state.recentUploads.size)
        assertEquals("Active Trainees: 5", state.recentUploads[0].status)
    }
}
