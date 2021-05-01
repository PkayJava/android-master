package ${pkg}.view

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PictureInPictureScreenModel
@Inject
constructor(
) : ViewModel() {

    private val _state = MutableStateFlow<DataState>(DataState.Permission)
    val state: StateFlow<DataState> = _state

    fun updateState(state: DataState) {
        _state.value = state
    }

    sealed class DataState {
        object Normal : DataState()
        object P2P : DataState()
        object Permission : DataState()
    }

}