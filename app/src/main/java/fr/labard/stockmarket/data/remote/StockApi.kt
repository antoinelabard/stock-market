package fr.labard.stockmarket.data.remote

import fr.labard.stockmarket.data.remote.dto.CompanyInforDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): CompanyInforDto

    companion object {
        const val API_KEY = "BFYNRU4N5LY3N4ZN"
        const val BASE_URL = "https://alphavantage.co"
    }
}