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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class CommunityFeedViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: GymmerApplication
    private lateinit var repository: GymmerRepository
    private lateinit var logManager: LogManager
    private lateinit var viewModel: CommunityFeedViewModel

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
        coEvery { repository.getFeed() } returns listOf(
            Post(id = UUID.randomUUID(), content = "Test Post", authorId = UUID.randomUUID())
        )
        coEvery { repository.getLeaderboard() } returns listOf(
            LeaderboardEntry(name = "User A", checkInCount = 10)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData updates uiState with repository data`() = runTest {
        viewModel = CommunityFeedViewModel(application)
        
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.posts.size)
        assertEquals("Test Post", state.posts[0].content)
        assertEquals(1, state.leaderboard.size)
        assertEquals("User A", state.leaderboard[0].userName)
    }

    @Test
    fun `loadData uses mock data when repository fails`() = runTest {
        coEvery { repository.getFeed() } throws Exception("Network Error")
        
        viewModel = CommunityFeedViewModel(application)
        
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.posts.size)
        assertEquals("John Doe", state.posts[0].userName) // Mock name from loadMockData()
    }

    @Test
    fun `onTabSelected updates selectedTab`() = runTest {
        viewModel = CommunityFeedViewModel(application)
        advanceUntilIdle()
        
        viewModel.onTabSelected(1)
        
        assertEquals(1, viewModel.uiState.value.selectedTab)
    }
}
