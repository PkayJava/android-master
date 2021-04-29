package ${pkg}.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ${pkg}.common.NetworkState
import ${pkg}.dto.AuthenticateResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ${pkg}.common.DispatcherProvider
import ${pkg}.repository.Repository
import javax.inject.Inject

@HiltViewModel
class LoginScreenModel
@Inject
constructor(
    private val repository: Repository,
    private val dispatchers: DispatcherProvider,
) : ViewModel() {

    var loginValue = mutableStateOf("")
    var passwordValue = mutableStateOf("")

    private val _state = MutableStateFlow<DataState>(DataState.Empty)
    val state: StateFlow<DataState> = _state

    fun reset(){
        _state.value = DataState.Empty
    }

    fun login(
        login: String,
        password: String,
    ) {

        if (login == "" || password == "") {
            _state.value = DataState.Error("Access Denied");
        }

        viewModelScope.launch(dispatchers.io) {
            _state.value = DataState.Loading
            var state = repository.login(username = login, password = password)
            when (state) {
                is NetworkState.Failure -> {
                    _state.value = DataState.Error(state.data)
                }
                is NetworkState.Success -> {
                    _state.value = DataState.Success(state.data)
                }
            }
        }
    }

    sealed class DataState {
        class Success(var data: AuthenticateResponseDto) : DataState()
        class Error(var data: String) : DataState()
        object Loading : DataState()
        object Empty : DataState()
    }

}