package fr.labard.stockmarket.domain.repository

import fr.labard.stockmarket.common.util.Resource
import fr.labard.stockmarket.domain.model.CompanyListing
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}