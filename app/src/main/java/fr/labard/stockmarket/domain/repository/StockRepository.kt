package fr.labard.stockmarket.domain.repository

import fr.labard.stockmarket.common.util.Resource
import fr.labard.stockmarket.domain.model.CompanyInfo
import fr.labard.stockmarket.domain.model.CompanyListing
import fr.labard.stockmarket.domain.model.IntradayInfo
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}