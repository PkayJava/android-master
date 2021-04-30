package ${pkg}.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BarcodeScreenModel
@Inject
constructor(
) : ViewModel() {

    private val _state = MutableStateFlow<DataState>(DataState.Permission)
    val state: StateFlow<DataState> = _state

    fun barcode() {
        _state.value = DataState.Barcode
    }

    fun barcodeReview() {
        _state.value = DataState.BarcodeReview
    }

    fun updateState(state: DataState) {
        _state.value = state
    }

    sealed class DataState {
        object Barcode : DataState()
        object Permission : DataState()
        object BarcodeReview : DataState()
    }

}