package com.m1x.gymmer.ui.screens.viewmodel

import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.utils.CoroutineManager
import com.m1x.gymmer.data.utils.LogManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: GymmerApplication
    private lateinit var coroutineManager: CoroutineManager
    private lateinit var logManager: LogManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        application = mockk(relaxed = true)
        coroutineManager = CoroutineManager(
            main = testDispatcher,
            io = testDispatcher,
            default = testDispatcher,
            unconfined = testDispatcher
        )
        
        // Mock LogManager.getInstance to avoid actual file system operations
        mockkObject(LogManager.Companion)
        logManager = mockk(relaxed = true)
        every { LogManager.getInstance(any(), any()) } returns logManager
        
        every { application.coroutineManager } returns coroutineManager
        
        viewModel = LoginViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `onEmailChanged updates state`() {
        viewModel.onEmailChanged("test@example.com")
        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `onPasswordChanged updates state`() {
        viewModel.onPasswordChanged("password123")
        assertEquals("password123", viewModel.uiState.value.password)
    }

    @Test
    fun `login with empty fields sets error`() {
        viewModel.login {}
        assertEquals("Please fill in all fields", viewModel.uiState.value.error)
    }

    @Test
    fun `login with valid fields succeeds after delay`() = runTest {
        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("password")
        
        var successCalled = false
        viewModel.login { successCalled = true }
        
        assertTrue(viewModel.uiState.value.isLoading)
        
        advanceUntilIdle()
        
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(successCalled)
    }
}
