package com.m1x.gymmer.data.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.m1x.gymmer.GymmerApplication
import com.m1x.gymmer.data.network.models.RegisterRequest

class RegistrationSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as GymmerApplication
        val registrationDao = app.database.registrationDao()
        val repository = app.repository

        val pending = registrationDao.getPendingRegistrations()
        if (pending.isEmpty()) return Result.success()

        var allSucceeded = true
        for (reg in pending) {
            try {
                repository.register(
                    RegisterRequest(
                        gymId = reg.gymId,
                        name = reg.name,
                        email = reg.email,
                        phone = reg.phone,
                        password = reg.password,
                        role = reg.role
                    )
                )
                registrationDao.deleteRegistration(reg)
            } catch (e: Exception) {
                // If it fails again, keep it in DB for next run
                allSucceeded = false
            }
        }

        return if (allSucceeded) Result.success() else Result.retry()
    }
}
