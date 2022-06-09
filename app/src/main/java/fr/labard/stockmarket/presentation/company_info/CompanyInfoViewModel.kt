package fr.labard.stockmarket.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.labard.stockmarket.common.util.Resource
import fr.labard.stockmarket.domain.repository.StockRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyinfoResult = async { repository.getCompanyinfo(symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }
            when (val result = companyinfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(company = result.data, isLoading = false, error = null)
                }
                is Resource.Error -> {
                    state = state.copy(company = null, isLoading = false, error = result.message)
                }
                else -> Unit
            }
            when (val result = intradayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(stockInfos = result.data ?: emptyList(), isLoading = false, error = null)
                }
                is Resource.Error -> {
                    state = state.copy(company = null, isLoading = false, error = result.message)
                }
                else -> Unit
            }
        }
    }
}