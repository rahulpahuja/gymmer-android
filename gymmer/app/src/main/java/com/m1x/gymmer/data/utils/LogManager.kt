package com.m1x.gymmer.data.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * LogManager that maintains 15 separate log files for different app components.
 * Uses applicationContext to avoid memory leaks and CoroutineManager for IO operations.
 */
class LogManager private constructor(
    context: Context,
    private val coroutineManager: CoroutineManager
) {

    private val appContext = context.applicationContext
    private val logFiles = mutableListOf<File>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    companion object {
        private const val TAG = "LogManager"
        private const val MAX_FILES = 15
        private const val FILE_PREFIX = "gym_log_"
        
        @Volatile
        private var INSTANCE: LogManager? = null

        fun getInstance(context: Context, coroutineManager: CoroutineManager): LogManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LogManager(context, coroutineManager).also { INSTANCE = it }
            }
        }
    }

    init {
        initLogFiles()
    }

    private fun initLogFiles() {
        val dir = File(appContext.filesDir, "logs")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        for (i in 0 until MAX_FILES) {
            val file = File(dir, "${FILE_PREFIX}$i.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            logFiles.add(file)
        }
        Log.d(TAG, "Initialized $MAX_FILES log files in ${dir.absolutePath}")
    }

    /**
     * Logs a message to a specific file index (0-14).
     * Writing is performed on an IO thread via CoroutineManager.
     */
    fun logToFile(index: Int, tag: String, message: String) {
        val timestamp = dateFormat.format(Date())
        val logEntry = "[$timestamp] [$tag]: $message\n"

        // Always log to Logcat immediately
        Log.d(tag, message)

        val safeIndex = if (index in 0 until MAX_FILES) index else 0
        val file = logFiles[safeIndex]

        // Offload file writing to IO thread
        coroutineManager.launchInExternalScope {
            try {
                FileOutputStream(file, true).use { output ->
                    output.write(logEntry.toByteArray())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to write log to file: ${file.name}", e)
            }
        }
    }

    fun info(category: LogCategory, message: String) {
        logToFile(category.ordinal, category.name, message)
    }

    enum class LogCategory {
        DASHBOARD,
        TRAIN,
        SCAN,
        WALLET,
        PROFILE,
        LOGIN,
        NETWORK,
        DATABASE,
        NOTIFICATIONS,
        AUTH,
        ANALYTICS,
        ERRORS,
        TRAINEES,
        STUDIO,
        SYSTEM
    }
}
