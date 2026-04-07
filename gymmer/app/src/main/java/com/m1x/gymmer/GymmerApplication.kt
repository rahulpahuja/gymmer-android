package com.m1x.gymmer

import android.app.Application
import com.m1x.gymmer.data.network.NetworkManager
import com.m1x.gymmer.data.utils.CoroutineManager
import com.m1x.gymmer.data.utils.LogManager
import com.m1x.gymmer.data.utils.RootDetection

class GymmerApplication : Application() {

    lateinit var coroutineManager: CoroutineManager
        private set
        
    lateinit var logManager: LogManager
        private set

    lateinit var networkManager: NetworkManager
        private set

    var isDeviceRooted: Boolean = false
        private set

    override fun onCreate() {
        super.onCreate()
        
        // Security Check: Root Detection
        isDeviceRooted = RootDetection.isDeviceRooted()
        
        coroutineManager = CoroutineManager()
        logManager = LogManager.getInstance(this, coroutineManager)
        networkManager = NetworkManager(logManager)

        logManager.info(LogManager.LogCategory.SYSTEM, "Application Started. Rooted: $isDeviceRooted")
    }
}
