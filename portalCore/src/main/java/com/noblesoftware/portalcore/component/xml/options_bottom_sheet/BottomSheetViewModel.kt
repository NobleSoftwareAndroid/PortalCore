package com.noblesoftware.portalcore.component.xml.options_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noblesoftware.portalcore.R
import com.noblesoftware.portalcore.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    var state = MutableStateFlow(BottomSheetState())
        private set

    private var job: Job? = null

    init {
    }

    fun onEvent(event: BottomSheetEvent) {
        when (event) {
            is BottomSheetEvent.OnSearch -> {
                state.update {
                    it.copy(keywords = event.keywords)
                }

                if (event.keywords.isEmpty()) return
                debounce {
                    val filter = state.value.selectOptions.filter { option ->
                        if (option.nameId != null) {
                            networkHelper.getString(option.nameId ?: R.string.empty_string)
                                .lowercase().contains(event.keywords.lowercase())
                        } else {
                            option.name.lowercase().contains(event.keywords.lowercase())
                        }
                    }
                    state.update { it.copy(filteredSelectOptions = filter) }
                }
            }

            is BottomSheetEvent.InitSelectOptions -> state.update {
                it.copy(selectOptions = event.options)
            }

            is BottomSheetEvent.OnSelect -> {
                state.update {
                    it.copy(
                        selectOptions = it.selectOptions.map { option ->
                            option.copy(isSelected = if (event.option.id == option.id) !option.isSelected else option.isSelected)
                        },
                        filteredSelectOptions = it.filteredSelectOptions.map { option ->
                            option.copy(isSelected = if (event.option.id == option.id) !option.isSelected else option.isSelected)
                        }
                    )
                }
            }

            BottomSheetEvent.ResetSelected -> {
                state.update {
                    it.copy(
                        selectOptions = it.selectOptions.map { option ->
                            option.copy(isSelected = false)
                        },
                        filteredSelectOptions = it.filteredSelectOptions.map { option ->
                            option.copy(isSelected = false)
                        }
                    )
                }
            }
        }
    }

    private fun debounce(action: () -> Unit) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Main) {
            delay(250)
            action.invoke()
        }
    }

}