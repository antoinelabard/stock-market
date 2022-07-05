package fr.labard.stockmarket.data.repository

import fr.labard.stockmarket.common.util.Resource
import fr.labard.stockmarket.data.csv.CSVParser
import fr.labard.stockmarket.data.local.StockDatabase
import fr.labard.stockmarket.data.mapper.toCompanyInfo
import fr.labard.stockmarket.data.mapper.toCompanyListing
import fr.labard.stockmarket.data.mapper.toCompanyListingEntity
import fr.labard.stockmarket.data.remote.StockApi
import fr.labard.stockmarket.domain.model.CompanyInfo
import fr.labard.stockmarket.domain.model.CompanyListing
import fr.labard.stockmarket.domain.model.IntradayInfo
import fr.labard.stockmarket.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            val dbIsEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !dbIsEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data."))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyLitings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao.searchCompanyListing("").map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load info")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            if (result.symbol == null) Resource.Error(message = "Couldn't load company info")
            else Resource.Success(result.toCompanyInfo())

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load company info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load company info")
        }
    }
}