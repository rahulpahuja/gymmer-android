package com.m1x.gymmer.data.network

import com.m1x.gymmer.data.database.entity.TraineeEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("trainees")
    suspend fun getTrainees(): List<TraineeEntity>

    @GET("trainees/{id}")
    suspend fun getTraineeDetails(@Path("id") id: String): TraineeEntity
}
