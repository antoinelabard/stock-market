package fr.labard.stockmarket.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingsEntities: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyLitings()

    @Query(
        """
            SELECT *
            FROM companylistingentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol
        """
    )
    suspend fun searchCompanyListing(query: String): List<CompanyListingEntity>
}