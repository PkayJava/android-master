package ${pkg}.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MenuScreenModel
@Inject
constructor(
) : ViewModel() {

    private val _state = MutableStateFlow<DataState>(DataState.Ok)
    val state: StateFlow<DataState> = _state

    sealed class DataState {
        object Ok : DataState()
    }

}