package com.mina.weatherapp.presentation.alerts.addalerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mina.weatherapp.data.alerts.AlertsRepository
import com.mina.weatherapp.data.alerts.local.entity.AlertActionType
import com.mina.weatherapp.data.alerts.local.entity.AlertConditionType
import com.mina.weatherapp.data.alerts.local.entity.AlertEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class AddAlertUiState {
    object Idle : AddAlertUiState()
    object Saving : AddAlertUiState()
    object Saved : AddAlertUiState()
    data class Error(val message: String) : AddAlertUiState()
}

class AddAlertViewModel(
    private val repo: AlertsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddAlertUiState>(AddAlertUiState.Idle)
    val uiState: StateFlow<AddAlertUiState> = _uiState.asStateFlow()

    fun save(
        title: String,
        lat: Double,
        lon: Double,
        start: Long,
        end: Long,
        condition: AlertConditionType,
        threshold: Double?,
        action: AlertActionType
    ) {
        if (title.isBlank()) {
            _uiState.value = AddAlertUiState.Error("Title is required")
            return
        }
        if (end <= start) {
            _uiState.value = AddAlertUiState.Error("End time must be after start time")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddAlertUiState.Saving
            try {
                repo.addAlert(
                    AlertEntity(
                        title = title,
                        latitude = lat,
                        longitude = lon,
                        startTimeMillis = start,
                        endTimeMillis = end,
                        conditionType = condition,
                        threshold = threshold,
                        actionType = action,
                        enabled = true
                    )
                )
                _uiState.value = AddAlertUiState.Saved
            } catch (e: Exception) {
                _uiState.value = AddAlertUiState.Error(e.message ?: "Failed to save alert")
            }
        }
    }

    fun reset() {
        _uiState.value = AddAlertUiState.Idle
    }
    fun showDateTimePicker(
        context: Context,
        initialMillis: Long = System.currentTimeMillis(),
        onPicked: (Long) -> Unit
    ) {
        val cal = Calendar.getInstance().apply { timeInMillis = initialMillis }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val pickedCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        pickedCal.set(Calendar.HOUR_OF_DAY, hour)
                        pickedCal.set(Calendar.MINUTE, minute)
                        onPicked(pickedCal.timeInMillis)
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

@Suppress("UNCHECKED_CAST")
class AddAlertViewModelFactory(private val repo: AlertsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AddAlertViewModel(repo) as T
}