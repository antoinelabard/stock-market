package fr.labard.stockmarket.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.labard.stockmarket.data.csv.CSVParser
import fr.labard.stockmarket.data.csv.CompanyListingsParser
import fr.labard.stockmarket.data.repository.StockRepositoryImpl
import fr.labard.stockmarket.domain.model.CompanyListing
import fr.labard.stockmarket.domain.repository.StockRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}