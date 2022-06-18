package fr.labard.stockmarket.data.mapper

import fr.labard.stockmarket.data.local.CompanyListingEntity
import fr.labard.stockmarket.data.remote.dto.CompanyInfoDto
import fr.labard.stockmarket.domain.model.CompanyInfo
import fr.labard.stockmarket.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() = CompanyListing(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyListing.toCompanyListingEntity() = CompanyListingEntity(
    name = name,
    symbol = symbol,
    exchange = exchange
)

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}