package fr.labard.stockmarket.presentation.company_info

import fr.labard.stockmarket.domain.model.CompanyInfo
import fr.labard.stockmarket.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val companyInfoError: String? = null,
) {
}