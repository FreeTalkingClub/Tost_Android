package com.tost.data.service

import com.tost.data.service.request.SaveGoalRequestParams
import com.tost.data.service.request.SaveWeeklyGoalParams
import com.tost.data.service.request.TostLoginRequestParams
import com.tost.data.service.response.GoalsResponse
import com.tost.data.service.response.NextProblemNumberResponse
import com.tost.data.service.response.ProblemStateResponse
import com.tost.data.service.response.TostLoginResponse
import retrofit2.http.*

/**
 * Created By Malibin
 * on 12월 11, 2020
 */

interface TostService {

    @POST("/login/google")
    suspend fun login(
        @Body loginRequestParams: TostLoginRequestParams
    ): TostLoginResponse

    @POST("/target")
    suspend fun saveGoal(
        @Header("token") token: String,
        @Body saveGoalRequestParams: SaveGoalRequestParams
    )

    @PUT("/target/solve")
    suspend fun saveWeeklyGoal(
        @Header("token") token: String,
        @Body saveWeeklyGoalParams: SaveWeeklyGoalParams,
    )

    @GET("/target")
    suspend fun getGoals(
        @Header("token") token: String
    ): GoalsResponse

    @GET("/part/{part}")
    suspend fun getProblemStateOf(
        @Header("token") token: String,
        @Path("part") part: String,
    ): ProblemStateResponse

    @GET("/solved/part/{part}")
    suspend fun getNextProblemNumber(
        @Header("token") token: String,
        @Path("part") part: String,
    ): NextProblemNumberResponse

    companion object {
        const val BASE_URL = "http://13.124.234.226:8080"
    }
}
