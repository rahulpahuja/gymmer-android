package com.m1x.gymmer.data.utils

import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager to handle coroutine dispatchers and scopes across the application.
 * This helps in making the code more testable by allowing dispatcher injection.
 */
class CoroutineManager(
    val main: CoroutineDispatcher = Dispatchers.Main,
    val io: CoroutineDispatcher = Dispatchers.IO,
    val default: CoroutineDispatcher = Dispatchers.Default,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
) {
    private val job = SupervisorJob()
    val externalScope = CoroutineScope(job + io)

    /**
     * Executes a block of code on the IO dispatcher.
     */
    suspend fun <T> io(block: suspend CoroutineScope.() -> T): T = withContext(io, block)

    /**
     * Executes a block of code on the Main dispatcher.
     */
    suspend fun <T> main(block: suspend CoroutineScope.() -> T): T = withContext(main, block)

    /**
     * Executes a block of code on the Default dispatcher.
     */
    suspend fun <T> default(block: suspend CoroutineScope.() -> T): T = withContext(default, block)

    /**
     * Launches a coroutine in the external scope (application-level scope).
     */
    fun launchInExternalScope(block: suspend CoroutineScope.() -> Unit): Job {
        return externalScope.launch(block = block)
    }
}
