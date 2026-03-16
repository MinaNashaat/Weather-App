package com.mina.weatherapp.presentation.alerts.alertsdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.alerts.AlertsRepository
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AlertsUiState {
    object Loading : AlertsUiState()
    data class Success(val data: List<AlertEntity>) : AlertsUiState()
    data class Error(val message: String) : AlertsUiState()
}

class AlertsViewModel(
    private val repo: AlertsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlertsUiState>(AlertsUiState.Loading)
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getAllAlerts().collect { list ->
                _uiState.value = AlertsUiState.Success(list)
            }
        }
    }

    fun delete(alert: AlertEntity) {
        viewModelScope.launch { repo.deleteAlert(alert) }
    }

    fun toggle(alert: AlertEntity, enabled: Boolean) {
        viewModelScope.launch { repo.setEnabled(alert, enabled) }
    }
}

@Suppress("UNCHECKED_CAST")
class AlertsViewModelFactory(private val repo: AlertsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AlertsViewModel(repo) as T
}